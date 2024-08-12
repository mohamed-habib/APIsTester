package com.habib.apistester.ui.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.habib.apistester.R

@Composable
fun Header(
	title: String,
	backAction: () -> Unit
) {
	Row(
		modifier = Modifier
			.height(64.dp)
			.fillMaxWidth()
			.background(color = Color(0xFF061744))
			.padding(10.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween
	) {
		Icon(
			modifier = Modifier
				.clickable {
					backAction()
				},
			imageVector = Icons.AutoMirrored.Filled.ArrowBack,
			contentDescription = stringResource(id = R.string.navigate_icon),
			tint = Color.White
		)
		Text(
			modifier = Modifier.padding(start = 5.dp)
				.weight(5f),
			text = title,
			textAlign = TextAlign.Start,
			maxLines = 1,
			overflow = TextOverflow.Ellipsis,
			style = MaterialTheme.typography.titleMedium,
			color = Color.White
		)
	}
}