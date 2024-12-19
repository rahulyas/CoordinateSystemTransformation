package com.rahul.coordinatesystemtransformation.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahul.coordinatesystemtransformation.R

@Composable
fun OutLinedInput(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    enableClick: Boolean = true,
    readOnly: Boolean = false,
    minLines: Int = 1,
    maxLines: Int = Int.MAX_VALUE,
    placeholder: String? = "",
    trailingIcon: @Composable (() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    keyboardType: KeyboardType = KeyboardType.Text,
    isSingleLine: Boolean = true,
    imeAction: ImeAction = ImeAction.Default,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    labelStyle: TextStyle = MaterialTheme.typography.labelSmall,
    placeholderStyle: TextStyle = TextStyle(
        fontStyle = FontStyle.Italic,
        color = MaterialTheme.colorScheme.primaryContainer
    ),
    labelColor: Color = MaterialTheme.colorScheme.secondaryContainer,
    textStyle: TextStyle = TextStyle(
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
        color = MaterialTheme.colorScheme.secondary
    ),
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.primaryContainer,
    isError: Boolean = false,
) {
    val emojiRegex = Regex("[\\p{So}]")
    Column(horizontalAlignment = Alignment.End) {
        OutlinedTextField(
            enabled = enableClick,
            modifier = modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            readOnly = readOnly,
            placeholder = {
                Text(
                    text = placeholder ?: "",
                    style = placeholderStyle,
                )
            },
            trailingIcon = trailingIcon,
            colors =
            OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.surfaceVariant,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedLabelColor = MaterialTheme.colorScheme.secondaryContainer,
                unfocusedLabelColor = MaterialTheme.colorScheme.primaryContainer,
            ),
            value = value,
            minLines = minLines,
            maxLines = if (isSingleLine) 1 else maxLines,
            singleLine = isSingleLine,
            textStyle = textStyle,
            label = {
                Text(
                    text = label,
                    style = labelStyle,
                    color = labelColor,
                )
            },
            onValueChange = {
                onValueChange(it.replace(emojiRegex, ""))
            },
            keyboardOptions =
            KeyboardOptions(
                keyboardType = keyboardType,
                imeAction = imeAction,
            ),

            visualTransformation = visualTransformation,
        )
        if (isError) {
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = stringResource(id = R.string.required),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutLinedInputPreview() {
    OutLinedInput(label = stringResource(R.string.email), value = "", onValueChange = {})
}