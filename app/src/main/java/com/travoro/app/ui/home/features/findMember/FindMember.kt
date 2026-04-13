package com.travoro.app.ui.home.features.findMember

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.imageLoader
import coil.request.ImageRequest
import com.travoro.app.ui.components.CustomTopBar
import com.travoro.app.ui.theme.TealCyan
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.BoundingBox
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.Marker
import androidx.core.graphics.toColorInt
import androidx.core.graphics.scale
import androidx.core.graphics.drawable.toDrawable
import androidx.core.graphics.createBitmap

@Composable
fun FindMemberScreen(
    homeNavController: NavController,
    viewModel: FindMemberViewModel = hiltViewModel(),
) {
    var mapView by remember { mutableStateOf<MapView?>(null) }
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.start()
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.stopTracking()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        CustomTopBar(
            title = "LIVE TELEMETRY",
            onBackClick = {
                homeNavController.popBackStack()
            },
        )

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.TopCenter,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.92f)
                    .fillMaxHeight(0.95f)
                    .padding(top = 16.dp)
                    .background(
                        MaterialTheme.colorScheme.surface,
                        RoundedCornerShape(20.dp),
                    )
                    .border(
                        1.dp,
                        TealCyan.copy(alpha = 0.3f),
                        RoundedCornerShape(20.dp),
                    ),
            ) {
                AndroidView(
                    factory = { ctx ->
                        Configuration.getInstance().userAgentValue = ctx.packageName
                        MapView(ctx).apply {
                            setTileSource(TileSourceFactory.MAPNIK)
                            controller.setZoom(5.0)
                            mapView = this
                        }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(20.dp)),
                )
            }
        }
    }

    LaunchedEffect(uiState) {
        val map = mapView ?: return@LaunchedEffect

        map.overlays.removeAll { it is FolderOverlay }

        when (val state = uiState) {
            is FindMemberUiState.Success -> {
                val members = state.members

                if (members.isEmpty()) {
                    map.invalidate()
                    return@LaunchedEffect
                }

                val folderOverlay = FolderOverlay()
                val points = mutableListOf<GeoPoint>()

                members.forEach { member ->
                    val point = GeoPoint(member.lat, member.lng)
                    points.add(point)

                    val marker = Marker(map).apply {
                        position = point
                        setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                        infoWindow = null
                        icon = createImageMarkerIcon(context, null)
                    }
                    folderOverlay.add(marker)
                    coroutineScope.launch {
                        val bitmap = fetchBitmapFromUrl(context, member.image)
                        if (bitmap != null) {
                            marker.icon = createImageMarkerIcon(context, bitmap)
                            map.invalidate()
                        }
                    }
                }

                map.overlays.add(folderOverlay)

                if (points.size == 1) {
                    map.controller.setZoom(15.0)
                    map.controller.setCenter(points.first())
                } else {
                    val boundingBox = BoundingBox.fromGeoPoints(points)
                    map.post {
                        map.zoomToBoundingBox(boundingBox, true, 150)
                    }
                }
            }

            is FindMemberUiState.NoActiveTrip -> {}
            else -> {}
        }
        map.invalidate()
    }
}

suspend fun fetchBitmapFromUrl(
    context: Context, url: String?
): Bitmap? {
    if (url.isNullOrEmpty()) return null

    val request = ImageRequest.Builder(context).data(url).allowHardware(false).build()

    val result = context.imageLoader.execute(request)
    return (result.drawable as? BitmapDrawable)?.bitmap
}

fun createImageMarkerIcon(context: Context, profilePic: Bitmap?): Drawable {
    val density = context.resources.displayMetrics.density
    val markerSize = 46f * density
    val pointerSize = 10f * density
    val strokeWidth = 2.5f * density

    val height = markerSize + pointerSize

    val bitmap = createBitmap(markerSize.toInt(), height.toInt())
    val canvas = Canvas(bitmap)

    val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    paint.color = "#00BFA5".toColorInt()
    paint.style = Paint.Style.FILL
    val path = Path().apply {
        moveTo(markerSize / 2f - pointerSize, markerSize - strokeWidth)
        lineTo(markerSize / 2f + pointerSize, markerSize - strokeWidth)
        lineTo(markerSize / 2f, height)
        close()
    }
    canvas.drawPath(path, paint)

    val radius = markerSize / 2f
    canvas.drawCircle(markerSize / 2f, radius, radius, paint)


    paint.color = "#0B132B".toColorInt()
    canvas.drawCircle(markerSize / 2f, radius, radius - strokeWidth, paint)


    if (profilePic != null) {
        val innerSize = (markerSize - strokeWidth * 2).toInt()
        val scaledBitmap = profilePic.scale(innerSize, innerSize, false)
        val shaderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        val shader = BitmapShader(scaledBitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        shaderPaint.shader = shader

        val matrix = Matrix()
        matrix.setTranslate(strokeWidth, strokeWidth)
        shader.setLocalMatrix(matrix)

        canvas.drawCircle(markerSize / 2f, radius, radius - strokeWidth, shaderPaint)
    }
    return bitmap.toDrawable(context.resources)
}