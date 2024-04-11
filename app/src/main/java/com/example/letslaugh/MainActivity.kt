package com.example.letslaugh

import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.addCallback
import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.activity.compose.setContent
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.rememberSplineBasedDecay
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.DrawerDefaults
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.letslaugh.common.Constants
import com.example.letslaugh.common.model.NavItem
import com.example.letslaugh.presentation.RootGraph
import com.example.letslaugh.presentation.ui.theme.LetsLaughTheme
import com.example.letslaugh.presentation.ui.theme.Typography
import com.razorpay.PaymentData
import com.razorpay.PaymentResultWithDataListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity(), PaymentResultWithDataListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!hasRequiredPermissions()) {
            ActivityCompat.requestPermissions(
                this, CAMERAX_PERMISSIONS, 0
            )
        }
        setContent {
            LetsLaughTheme {
                val navController = rememberNavController()
                LetsLaughNavGraph(Constants.navBarItems, navController)
            }
        }
    }

    private fun hasRequiredPermissions(): Boolean {
        return CAMERAX_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(
                applicationContext,
                it
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    companion object {
        private val CAMERAX_PERMISSIONS = arrayOf(
            android.Manifest.permission.CAMERA,
            android.Manifest.permission.RECORD_AUDIO,
        )
    }

    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        Log.i("PaymentData", "${p1?.data}")
        Log.i("PaymentData", "$p0")
        Toast.makeText(this, "Success", Toast.LENGTH_LONG).show()
    }

    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.i("PaymentData", "$p0")
        Log.i("PaymentData", "$p1")
        Log.i("PaymentData", "${p2?.data}")
        Toast.makeText(this, "Failure", Toast.LENGTH_LONG).show()
    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
private fun LetsLaughNavGraph(items: Array<NavItem>, navController: NavHostController) {
    var drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val drawerWidth = DrawerDefaults.MaximumDrawerWidth
    val selectedItem = remember { mutableStateOf(items[0]) }
    val translationX = remember {
        Animatable(0f)
    }
    val decay = rememberSplineBasedDecay<Float>()
    translationX.updateBounds(0f, drawerWidth.value + 300)

    val onBack = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher
    onBack?.addCallback {
        if (Constants.navBarItems.takeLastWhile {
                it.route == (navController.currentBackStackEntry?.destination?.route ?: "")
            }.toList().isNotEmpty()) {
            selectedItem.value = Constants.navBarItems.takeLastWhile {
                it.route == (navController.currentBackStackEntry?.destination?.route ?: "")
            }.first()
        }
    }

    val draggableState = rememberDraggableState(onDelta = { dragAmount ->
        scope.launch {
            translationX.snapTo(translationX.value + dragAmount)
            drawerState = if (translationX.value == 0f) {
                DrawerState(DrawerValue.Closed)
            } else {
                DrawerState(DrawerValue.Open)
            }
            Log.d("OnNavbarDrag", "${drawerState.currentValue}")
        }
    })

    Box {
        ModalDrawerSheet(
            windowInsets = WindowInsets(top = 200.dp, bottom = 200.dp)
        ) {
            items.map {
                NavigationDrawerItem(
                    modifier = Modifier
                        .padding(start = 20.dp),
                    colors = NavigationDrawerItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                        selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                        selectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.8f)
                    ),
                    shape = RoundedCornerShape(topStart = 30.dp, bottomStart = 30.dp),
                    label = { Text(text = it.name, style = Typography.labelMedium) },
                    selected = it == selectedItem.value,
                    icon = {
                        Icon(
                            painter = rememberVectorPainter(image = it.icon),
                            contentDescription = it.route,
                        )
                    },
                    onClick = {
                        selectedItem.value = it
                        navController.navigate(it.route)
                        scope.launch {
                            translationX.animateTo(0f)
                            drawerState.apply {
                                close()
                            }
                        }
                    }
                )
            }
        }
        Scaffold(
            modifier = Modifier
                .graphicsLayer {
                    this.translationX = translationX.value
                    val scale = lerp(1f, 0.9f, (translationX.value / drawerWidth.value))
                    this.scaleX = scale
                    this.scaleY = scale
                    this.clip = translationX.value != 0f
                    this.shape = RoundedCornerShape(topStart = 20.dp, bottomStart = 20.dp)
                }
                .draggable(draggableState, Orientation.Horizontal, onDragStopped = {
                    val decayX = decay.calculateTargetValue(translationX.value, it)
                    scope.launch {
                        val targetX = if (decayX > (drawerWidth.value * 0.5)) {
                            drawerWidth.value
                        } else {
                            0f
                        }
                        val canReachTargetWithDecay =
                            (decayX > targetX && targetX == drawerWidth.value) || (decayX < targetX && targetX == 0f)
                        if (canReachTargetWithDecay) {
                            translationX.animateDecay(it, decay)
                        } else {
                            translationX.animateTo(targetX, initialVelocity = it)
                        }
                    }
                }),
            topBar = {
                Card(
                    shape = RoundedCornerShape(0.dp)
                ) {
                    Row {
                        Icon(painter = rememberVectorPainter(image = Icons.Default.Menu),
                            contentDescription = "menu",
                            modifier = Modifier
                                .padding(top = 5.dp, start = 20.dp)
                                .clickable {
                                    scope.launch {
                                        if (drawerState.isOpen) {
                                            translationX.animateTo(0f)
                                        } else {
                                            translationX.animateTo(drawerWidth.value + 300)
                                        }
                                        if (drawerState.isOpen) {
                                            drawerState.apply {
                                                close()
                                            }
                                        } else {
                                            drawerState.apply {
                                                open()
                                            }
                                        }
                                        Log.d("MenuCLick", "${drawerState.currentValue}")
                                    }
                                })
                        Text(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(5.dp),
                            textAlign = TextAlign.Center,
                            text = "Lets Laugh",
                            style = Typography.titleLarge
                        )
                    }
                }
            },
        ) {
            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                Image(
                    painter = painterResource(id = R.drawable.app_background),
                    contentDescription = "image",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                RootGraph(navController, it)
            }
        }
    }
}



