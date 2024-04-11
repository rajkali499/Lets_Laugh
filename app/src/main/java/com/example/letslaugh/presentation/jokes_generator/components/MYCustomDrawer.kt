package com.example.letslaugh.presentation.jokes_generator.components

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import com.example.letslaugh.common.model.NavItem

@Composable
fun LetsLaughDrawer(title : String,items: Array<NavItem>){
    ModalDrawerSheet {
        Text(
            "Lets Laugh",
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge
        )
        HorizontalDivider()
        items.map {
            NavigationDrawerItem(
                colors = NavigationDrawerItemDefaults.colors(
                    selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                    selectedContainerColor = MaterialTheme.colorScheme.primary.copy(0.8f)
                ),
                shape = RoundedCornerShape(0.dp),
                label = { Text(text = it.name) },
                selected = true,
                icon = {
                    Icon(
                        painter = rememberVectorPainter(image = it.icon),
                        contentDescription = it.route,
                    )
                },
                onClick = {
//                    selectedItem.value = it
//                    navController.navigate(it.route)
//                    scope.launch {
//                        drawerState.apply {
//                            close()
//                        }
//                    }
                }
            )
        }
    }
}