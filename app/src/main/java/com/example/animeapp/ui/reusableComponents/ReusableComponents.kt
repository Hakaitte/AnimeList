package com.example.animeapp.ui.reusableComponents

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.fastForEachIndexed
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.example.animeapp.R
import com.example.animeapp.ui.theme.AnimeAppTheme

/**
 * A reusable single-choice segmented button component.
 * @param options The list of options to display.
 * @param selectedIndex The index of the currently selected option.
 * @param modifier The modifier to be applied to the segmented button row.
 * @param onOptionSelected The callback to be invoked when an option is selected.
 */

data class BottomNavItem(
    val labelResId: Int,
    val icon: ImageVector,
    val route: String
)

@Composable
fun BottomNavigationBar(navController: NavController, currentRoute: String?) {
    val items = listOf(
        BottomNavItem(R.string.bottom_nav_search, Icons.Default.Search, "search"),
        BottomNavItem(R.string.bottom_nav_profile, Icons.Default.Person, "profile"),
    )

    NavigationBar { // Material 3 Bottom Navigation Bar
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                selected = currentRoute == item.route,
                onClick = {
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    }
}

@Composable
fun SingleChoiceSegmentedButton(
    options: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit = {}
) {
    SingleChoiceSegmentedButtonRow(modifier = modifier) {
        options.forEachIndexed { index, label ->
            SegmentedButton(
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size
                ),
                onClick = { onOptionSelected(index) },
                selected = index == selectedIndex,
                label = { Text(label) }
            )
        }
    }
}

/**
 * A simple info message component (Text in a Box).
 * @param message The message to be displayed.
 * @param modifier The modifier to be applied to the box.
 */
@Composable
fun InfoMessage(
    message: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = message,
            textAlign = TextAlign.Center
        )
    }
}

/**
 * A simple loading indicator component (CircularProgressIndicator in a Box).
 * @param modifier The modifier to be applied to the box.
 */
@Composable
fun LoadingIndicator(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

/**
 * A simple error message component (Texts in a Column).
 * The first text is the title in colorScheme.error color
 * and the second text is the error message (normal color).
 * @param errorTitle The title of the error message ("Error" is the default value).
 * @param errorMessage The error message to be displayed.
 * @param modifier The modifier to be applied to the column.
 */
@Composable
fun ErrorMessage(
    errorTitle: String = "Error",
    errorMessage: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = errorTitle,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.error
        )
        Text(
            text = errorMessage,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

/**
 * A reusable dropdown menu component it implements a Spinner-like experience. It's created using
 * ExposedDropdownMenuBox and ExposedDropdownMenu and TextField.
 * @param options The list of options to display in the dropdown menu.
 * @param selectedIndex The index of the currently selected option.
 * @param modifier The modifier to be applied to the dropdown menu.
 * @param onOptionSelected The callback to be invoked when an option is selected.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenu(
    options: List<String>,
    selectedIndex: Int,
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }
    var textValue by remember { mutableStateOf(options[selectedIndex]) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier
    ) {
        TextField(
            value = textValue,
            onValueChange = {    },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable, true),
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
            },
            readOnly = true,
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
            singleLine = true,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEachIndexed { index, option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        // change the text value to the selected option
                        textValue = option
                        // call the onOptionSelected callback with the selected index to notify
                        // the parent composable about the selection
                        onOptionSelected(index)
                        // close the dropdown menu
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

/**
 * A reusable chip group component that allows for multiple selections.
 * It uses a FlowRow to display the chips in a flow layout which moves
 * the elements that don't fit the screen to the next row. In this composable we have to option
 * to either display the options in a single line or in a vertical scrolling list. The switch
 * between the two is done by clicking on the leading chip that expands the list into a vertical
 * scrolling list (by default it's a horizontal scrolling list).
 * @param options The list of options to display as chips.
 * @param selectionState The list of booleans representing the selection state of each option.
 * @param modifier The modifier to be applied to the chip group.
 * @param onOptionSelected The callback to be invoked when an option is selected.
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ChipGroupReflow(
    options: List<String>,
    selectionState: List<Boolean>,
    modifier: Modifier = Modifier,
    onOptionSelected: (Int) -> Unit = {}
) {
    require(options.size == selectionState.size) {
        "Options and selectionState must have the same size"
    }
    var selected by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        FlowRow(
            modifier =
                Modifier
                    .fillMaxWidth(1f)
                    .wrapContentHeight(align = Alignment.Top)
                    .then(
                        if (selected) {
                            Modifier.verticalScroll(rememberScrollState())
                        } else {
                            Modifier.horizontalScroll(rememberScrollState())
                        }
                    ),
            horizontalArrangement = Arrangement.Start,
            maxLines = if (!selected) 1 else Int.MAX_VALUE,
        ) {
            /*
             * When chip lists exceed the available horizontal screen space, one option is to
             * provide a leading chip that expands the list into a vertical scrolling list. This
             * ensures all options are accessible while maintaining the position of the content
             * below the chip list.
             */
            FilterChip(
                selected = selected,
                modifier =
                    Modifier
                        .padding(horizontal = 4.dp)
                        .align(alignment = Alignment.CenterVertically),
                onClick = { selected = !selected },
                label = { Text(stringResource(R.string.show_all)) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.List,
                        contentDescription = "Localized Description",
                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                    )
                }
            )
            Box(
                Modifier
                    .height(FilterChipDefaults.Height)
                    .align(alignment = Alignment.CenterVertically)
            ) {
                VerticalDivider()
            }
            options.fastForEachIndexed { index, element ->
                FilterChip(
                    selected = selectionState[index],
                    modifier =
                        Modifier
                            .padding(horizontal = 4.dp)
                            .align(alignment = Alignment.CenterVertically),
                    onClick = { onOptionSelected(index) },
                    label = { Text(element) }
                )
            }
        }
    }
}

@Preview(showSystemUi = false, showBackground = true)
@Composable
fun ExposedDropdownMenuSamplePreview() {
    AnimeAppTheme {
        ExposedDropdownMenu(options = listOf("Option 1", "Option 2", "Option 3"), 1) {
            // Handle option selection
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ChipGroupReflowSamplePreview() {
    val colorNames =
        listOf(
            "Blue",
            "Yellow",
            "Red",
            "Orange",
            "Black",
            "Green",
            "White",
            "Magenta",
            "Gray",
            "Transparent"
        )

    var selectionState = remember {
        mutableStateListOf<Boolean>().apply {
            addAll(List(colorNames.size) { false })
        }
    }
    AnimeAppTheme {
        ChipGroupReflow(colorNames, selectionState) {
            // Handle chip click
            selectionState[it] = !selectionState[it]
        }
    }
}
@Preview(showBackground = true)
@Composable
fun InfoMessagePreview() {
    AnimeAppTheme {
       InfoMessage(
            message = "This is an info message",
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun LoadingIndicatorPreview() {
    AnimeAppTheme {
       LoadingIndicator(
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun ErrorMessagePreview() {
    AnimeAppTheme {
        ErrorMessage(
            errorTitle = "Error",
            errorMessage = "This is an error message",
            modifier = Modifier.padding(16.dp)
        )
    }
}
@Preview(showBackground = true)
@Composable
fun SingleChoiceSegmentedButtonPreview() {
    AnimeAppTheme {
        SingleChoiceSegmentedButton(
            options = listOf("Option 1", "Option 2"),
            selectedIndex = 1,
            onOptionSelected = {}
        )
    }
}