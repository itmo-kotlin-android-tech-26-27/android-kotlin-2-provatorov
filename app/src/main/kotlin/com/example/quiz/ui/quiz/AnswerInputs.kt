package com.example.quiz.ui.quiz

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp

@Composable
fun ChoiceAnswer(
    options: List<String>,
    selectedOption: Int?,
    onSelect: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.selectableGroup(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        options.forEachIndexed { index, option ->
            val selected = index == selectedOption
            OutlinedCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .selectable(
                            selected = selected,
                            role = Role.RadioButton,
                            onClick = { onSelect(index) },
                        )
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    RadioButton(selected = selected, onClick = null)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(option, modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun OpenAnswer(answer: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = answer,
        onValueChange = onChange,
        label = { Text("Ваш ответ") },
        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
        modifier = Modifier.fillMaxWidth(),
    )
}

@Composable
fun MatchAnswer(
    leftItems: List<String>,
    rightItems: List<String>,
    matchedRights: Map<Int, Int>,
    selectedLeft: Int?,
    onLeftClick: (Int) -> Unit,
    onRightClick: (Int) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "Нажмите на элемент слева, затем на подходящий элемент справа. " +
                "Одинаковые номера означают пару. Чтобы изменить пару, снова нажмите на элемент слева.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                leftItems.forEachIndexed { index, text ->
                    MatchItem(
                        text = text,
                        number = index + 1,
                        isPaired = index in matchedRights,
                        isSelected = index == selectedLeft,
                        onClick = { onLeftClick(index) },
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                rightItems.forEachIndexed { index, text ->
                    val leftIndex = matchedRights.entries.find { it.value == index }?.key
                    MatchItem(
                        text = text,
                        number = if (leftIndex == null) null else leftIndex + 1,
                        isPaired = leftIndex != null,
                        isSelected = false,
                        onClick = { onRightClick(index) },
                    )
                }
            }
        }
    }
}

@Composable
private fun MatchItem(
    text: String,
    number: Int?,
    isPaired: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val containerColor = if (isPaired) {
        MaterialTheme.colorScheme.secondaryContainer
    } else {
        MaterialTheme.colorScheme.surface
    }
    val border = if (isSelected) {
        BorderStroke(2.dp, MaterialTheme.colorScheme.primary)
    } else {
        BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant)
    }

    OutlinedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.outlinedCardColors(containerColor = containerColor),
        border = border,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = number?.toString() ?: "–",
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(24.dp),
            )
            Text(text, modifier = Modifier.weight(1f))
        }
    }
}
