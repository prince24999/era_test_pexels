package com.vp.era_test_pexels

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vp.era_test_pexels.control.SearchHistoryManager
import com.vp.era_test_pexels.control.encodeUrl
import com.vp.era_test_pexels.control.getColor
import com.vp.era_test_pexels.control.getOrientation
import com.vp.era_test_pexels.control.getPageNumber
import com.vp.era_test_pexels.control.getPhotosPerPage
import com.vp.era_test_pexels.control.getSize
import com.vp.era_test_pexels.control.isInternetAvailable
import com.vp.era_test_pexels.ui.PhotoList
import com.vp.era_test_pexels.ui.main.SharedTopBar


class Search : ComponentActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {


            Scaffold(modifier = Modifier.fillMaxSize()
                ,
                topBar = {
                    SharedTopBar("Photo Search")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(color = Color.White)
                        ,
                    contentAlignment = Alignment.TopCenter
                ) {
                    SearchForm()

                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCompleteTextField(
    suggestions: List<String>,
    onItemSelected: (String) -> Unit
) {
    var text by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it }
    ) {
        TextField(
            value = text,
            singleLine = true,
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                focusedContainerColor = Color.Transparent
            ),
            onValueChange = { newText ->
                text = newText // update when type
                expanded = suggestions.any { item -> item.contains(newText, ignoreCase = true) }
            },
            modifier = Modifier.menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true).fillMaxWidth(),
            label = { Text("Type something here to search...")
            }
        )

        ExposedDropdownMenu(modifier = Modifier.background(Color.White).padding(0.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            suggestions.filter { it.contains(text, ignoreCase = true) }.forEach { item ->
                DropdownMenuItem(
                    text = { Text(item) },
                    onClick = {
                        text = item
                        expanded = false
                        onItemSelected(item) // do when selected
                    }
                )
            }
        }
    }

    // push value out
    LaunchedEffect(text) {
        onItemSelected(text) // update value when user input new string
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForm() {
    val context = LocalContext.current
    // Get search history
    val searchHistoryManager = SearchHistoryManager(context)
    val history = searchHistoryManager.getSearchHistory()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val optionsOrientation = listOf("landscape", "portrait", "square")
        val optionsSize = listOf("large", "medium", "small")
        val optionsColor = listOf("","red", "orange", "yellow","green","turquoise","blue","violet","pink","brown","black","gray","white")
        var selectedSize by remember { mutableStateOf(optionsSize[0]) }
        var selectedColor by remember { mutableStateOf(optionsColor[0]) }
        var selectedOrientation by remember { mutableStateOf(optionsOrientation[0]) }
        var expandedOrientation by remember { mutableStateOf(false) }
        var expandedSize by remember { mutableStateOf(false) }
        var expandedColor by remember { mutableStateOf(false) }


        // query string
        var searchText by remember { mutableStateOf("") }

        //val validPattern = Regex("^[a-zA-Z0-9]+$")

        var localeText by remember { mutableStateOf("en-US") }
        val pageNumberText by remember { mutableStateOf("1") }
        val perPageText by remember { mutableFloatStateOf(15f) }

        // Query String Input
         Box(modifier = Modifier.padding(5.dp)) {

             AutoCompleteTextField(
                 suggestions = history,
                 onItemSelected = { selectedText ->
                     Log.d("History","Selected: $selectedText")
                     searchText = selectedText
                 }
             )


        }

        // Orientation Input
         Box(modifier = Modifier.padding(5.dp)) {
            ExposedDropdownMenuBox(

                expanded = expandedOrientation,
                onExpandedChange = { expandedOrientation = !expandedOrientation }
            ) {
                TextField(
                    value = selectedOrientation,
                    onValueChange = {selectedOrientation = it},
                    readOnly = true,
                    label = { Text("Select Orientation") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent),
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth(),
                )
                ExposedDropdownMenu(modifier = Modifier.background(Color.White).padding(0.dp),
                    expanded = expandedOrientation,
                    onDismissRequest = { expandedOrientation = false }
                ) {
                    optionsOrientation.forEach { option ->

                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    selectedOrientation = option
                                    expandedOrientation = false
                                },

                            )

                    }
                }
            }
        }

        // Size Input
         Box(modifier = Modifier.padding(5.dp)) {
            ExposedDropdownMenuBox(
                expanded = expandedSize,
                onExpandedChange = {expandedSize = !expandedSize }
            ) {
                TextField(
                    value = selectedSize,
                    onValueChange = {selectedSize = it},
                    readOnly = true,
                    label = { Text("Select Size") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent),
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth()

                )
                ExposedDropdownMenu(modifier = Modifier.background(Color.White).padding(0.dp),
                    expanded = expandedSize,
                    onDismissRequest = { expandedSize = false }
                ) {
                    optionsSize.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
                            onClick = {
                                selectedSize = option
                                expandedSize = false
                            },
                        )
                    }
                }
            }
        }

        // Color Input
         Box(modifier = Modifier.padding(5.dp)) {
            ExposedDropdownMenuBox(
                expanded = expandedColor,
                onExpandedChange = {expandedColor = !expandedColor }
            ) {
                TextField(
                    value = selectedColor,
                    onValueChange = {selectedColor = it},
                    readOnly = true,
                    label = { Text("Select Color") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.Transparent,
                        focusedContainerColor = Color.Transparent),
                    modifier = Modifier
                        .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                        .fillMaxWidth()


                )
                ExposedDropdownMenu(modifier = Modifier.background(Color.White).padding(0.dp)
                    ,
                    expanded = expandedColor,
                    onDismissRequest = { expandedColor = false }
                ) {
                    optionsColor.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option, color = Color.Black) },
                            onClick = {
                                selectedColor = option
                                expandedColor = false
                            },
                            //modifier = Modifier.clip(RoundedCornerShape(12.dp))
                        )
                    }
                }
            }
        }

        // Locale String Input
         Box(modifier = Modifier.padding(5.dp)) {

            TextField(
                    localeText, {localeText = it}, Modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
                    .border(
                        width = 1.dp,
                        color = Color.Transparent,
                        shape = RoundedCornerShape(0.dp)
                    ), label = { Text("Locale")},
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent
                )

            )
        }



        // Search
        Box(modifier = Modifier.padding(10.dp)) {

            SubmitSearch(searchText, getOrientation(selectedOrientation), getSize(selectedSize), getColor(selectedColor), encodeUrl(localeText), getPageNumber(pageNumberText), getPhotosPerPage(perPageText))
        }
    }
}

@Composable
fun SubmitSearch(query: String, orientation: String, size: String, color: String, locale: String, pageNumber: Int, perPage: Int) {
    val context = LocalContext.current
    val searchHistoryManager = SearchHistoryManager(context)

    Button(onClick = {

        // Check internet connection
        if(isInternetAvailable(context))
        {
            // Check if query is empty, if so, show toast. if not, keep going
            if (query.isEmpty()) {
                Toast.makeText(context, "Please type something to search. Ex: nature, tiger, snow, ...", Toast.LENGTH_SHORT).show()
            }
            else
            {
                // Save search query to store
                searchHistoryManager.saveSearchQuery(query)

                Log.d("Search", "SubmitSearch: $query $orientation $size $color $locale $pageNumber $perPage")

                val intent = Intent(context, PhotoList::class.java).apply()
                {
                    putExtra("query", encodeUrl(query))
                    putExtra("orientation", orientation)
                    putExtra("size", size)
                    putExtra("color", color)
                    putExtra("locale", locale)
                    putExtra("pageNumber", pageNumber)
                    putExtra("perPage", perPage)
                }
                context.startActivity(intent)
            }
        }
        else
        {
            Toast.makeText(context, "Please connect to internet", Toast.LENGTH_SHORT).show()
        }


    }
        ,
        modifier = Modifier
            .border(1.dp, Color.DarkGray, RoundedCornerShape(30.dp))
            .background(Color.Transparent, RoundedCornerShape(30.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.DarkGray
        )
    ) {
        Text("Search", fontSize = 16.sp)
    }
}


