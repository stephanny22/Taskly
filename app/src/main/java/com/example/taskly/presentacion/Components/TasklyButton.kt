package com.example.taskly.presentacion.Components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.taskly.presentacion.Config.OrangePrimary

@Composable
fun TasklyButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    outlined: Boolean = false,
    leadingIcon: ImageVector? = null,
) {
    if (outlined) {
        OutlinedButton(
            onClick  = onClick,
            enabled  = enabled,
            shape    = RoundedCornerShape(12.dp),
            border   = ButtonDefaults.outlinedButtonBorder.copy(width = 2.dp),
            colors   = ButtonDefaults.outlinedButtonColors(
                contentColor = OrangePrimary,
                disabledContentColor = Color.Gray,
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            leadingIcon?.let { Icon(it, null, modifier = Modifier.padding(end = 8.dp)) }
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    } else {
        Button(
            onClick  = onClick,
            enabled  = enabled,
            shape    = RoundedCornerShape(12.dp),
            colors   = ButtonDefaults.buttonColors(
                containerColor         = OrangePrimary,
                contentColor           = Color.White,
                disabledContainerColor = Color.LightGray,
                disabledContentColor   = Color.White,
            ),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 4.dp,
                pressedElevation = 2.dp,
            ),
            modifier = modifier
                .fillMaxWidth()
                .height(52.dp),
        ) {
            leadingIcon?.let { Icon(it, null, modifier = Modifier.padding(end = 8.dp)) }
            Text(text, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}