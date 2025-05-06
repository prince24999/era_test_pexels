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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
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
            var selectedIndex by remember { mutableIntStateOf(0) }

            Scaffold(modifier = Modifier.fillMaxWidth(),
                containerColor = Color.White,

                topBar = {
                    SharedTopBar("Photo Search")

                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    SearchForm()

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchForm() {
    val context = LocalContext.current
    val searchHistoryManager = remember { SearchHistoryManager(context) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        val optionsOrientation = listOf("landscape", "portrait", "square")
        val optionsSize = listOf("large", "medium", "small")
        val optionsColor = listOf("","red", "orange", "yellow","green","turquoise","blue","violet","pink","brown","black","gray","white")
        var selectedSize by remember { mutableStateOf(optionsSize[0]) }
        var selectedColor by remember { mutableStateOf(optionsColor[0]) }
        var selecteOrientation by remember { mutableStateOf(optionsOrientation[0]) }
        var expandedOrientation by remember { mutableStateOf(false) }
        var expandedSize by remember { mutableStateOf(false) }
        var expandedColor by remember { mutableStateOf(false) }


        // query string , validate only allow char, number
        var searchText by remember { mutableStateOf("") }
        var errorMessage by remember { mutableStateOf<String?>(null) }
        //val validPattern = Regex("^[a-zA-Z0-9]+$")

        var localeText by remember { mutableStateOf("") }
        var pageNumberText by remember { mutableStateOf("1") }
        var perPageText by remember { mutableStateOf(15f) }

        // Query String Input
         Box(modifier = Modifier.padding(5.dp)) {
            TextField(
                value = searchText,
                onValueChange = {
                    searchText = it
                    errorMessage = when {
                        searchText.isBlank() -> "Please enter a search query"

                        else -> null
                    }
                                },
                label = { Text("Type something here for Search") },
                isError = errorMessage != null,
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
                 //   .clip(RoundedCornerShape(16.dp)),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
//                )
            )



        }

        // Orientation Input
         Box(modifier = Modifier.padding(5.dp)) {
            ExposedDropdownMenuBox(

                expanded = expandedOrientation,
                onExpandedChange = { expandedOrientation = !expandedOrientation }
            ) {
                TextField(
                    value = selecteOrientation,
                    onValueChange = {selecteOrientation = it},
                    readOnly = true,
                    label = { Text("Select Orientation") },
                    modifier = Modifier
                        .menuAnchor()
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
                                    selecteOrientation = option
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
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        //.clip(RoundedCornerShape(16.dp)),

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
                            //modifier = Modifier.clip(RoundedCornerShape(12.dp))
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
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth()
                        //.clip(RoundedCornerShape(3.dp)),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.White,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent
//                    )
                )
                ExposedDropdownMenu(modifier = Modifier.background(Color.White).padding(0.dp),
                    expanded = expandedColor,
                    onDismissRequest = { expandedColor = false }
                ) {
                    optionsColor.forEach { option ->
                        DropdownMenuItem(
                            text = { Text(option) },
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
                value = localeText,
                onValueChange = {localeText = it},
                label = { Text("Locale") },
                modifier = Modifier
                    .background(color = Color.Transparent)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(5.dp)),
//                colors = TextFieldDefaults.colors(
//                    focusedContainerColor = Color.White,
//                    focusedIndicatorColor = Color.Transparent,
//                    unfocusedIndicatorColor = Color.Transparent
//                )
            )
        }

//        // Page number Input
//         Box(modifier = Modifier.padding(5.dp)) {
//            TextField(
//                value = pageNumberText,
//                onValueChange = {pageNumberText = it},
//                label = { Text("Page number") },
//                modifier = Modifier
//                    .background(color = Color.Transparent)
//                    .fillMaxWidth()
//                    .clip(RoundedCornerShape(5.dp)),
////                colors = TextFieldDefaults.colors(
////                    focusedContainerColor = Color.White,
////                    focusedIndicatorColor = Color.Transparent,
////                    unfocusedIndicatorColor = Color.Transparent
////                )
//            )
//        }
//
//        // Page number Input
//         Box(modifier = Modifier.padding(5.dp)) {
////            TextField(
////                value = perPageText,
////                onValueChange = {perPageText = it},
////
////                label = { Text("Photos per page") },
////                modifier = Modifier
////                    .background(color = Color.Transparent)
////                    .fillMaxWidth()
////                    .clip(RoundedCornerShape(16.dp)),
////                colors = TextFieldDefaults.colors(
////                    focusedContainerColor = Color.White,
////                    focusedIndicatorColor = Color.Transparent,
////                    unfocusedIndicatorColor = Color.Transparent
////                )
////            )
//             Column {
//                 Text("Photos per Page: ${perPageText.toInt()}",
//                     fontSize = 13.sp, modifier = Modifier.padding(vertical = 5.dp))
//
//                 Slider(
//                     value = perPageText,
//                     onValueChange = { perPageText = it },
//                     valueRange = 1f..80f, // from 1 to 80
//                     steps = 78, // split to integer steps
//                     modifier = Modifier
//                         .padding(vertical = 5.dp)
//                         .height(6.dp),
//                     colors = SliderDefaults.colors(
//                         thumbColor = Color.Red,
//                         activeTickColor = Color.Gray,
//                         inactiveTickColor = Color.Gray,
//                         activeTrackColor = Color.White,
//                         inactiveTrackColor = Color.White
//                     )
//                 )
//             }
//
//        }

        // Search
        Box(modifier = Modifier.padding(10.dp)) {
            // Save search query to store
            searchHistoryManager.saveSearchQuery("query_history")

            SubmitSearch(encodeUrl(searchText), getOrientation(selecteOrientation), getSize(selectedSize), getColor(selectedColor), encodeUrl(localeText), getPageNumber(pageNumberText), getPhotosPerPage(perPageText))
        }
    }
}

@Composable
fun SubmitSearch(query: String, orientation: String, size: String, color: String, locale: String, pageNumber: Int, perPage: Int) {
    val context = LocalContext.current

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
                Log.d("Search", "SubmitSearch: $query $orientation $size $color $locale $pageNumber $perPage")
                val intent = Intent(context, PhotoList::class.java).apply()
                {
                    putExtra("query", query)
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
            .border(2.dp, Color.Gray, RoundedCornerShape(15.dp))
            .background(Color.Transparent, RoundedCornerShape(15.dp)),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = Color.DarkGray
        )
    ) {
        Text("Search", fontSize = 16.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(searchHistory: List<String>, searchText: String, onSearch: (String) -> Unit) {
    var query by remember { mutableStateOf(searchText) }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = isDropdownExpanded,
        onExpandedChange = { isDropdownExpanded = it }
    ) {
        TextField(
            value = query,
            onValueChange = { newText ->
                query = newText
                isDropdownExpanded = searchHistory.any { it.contains(newText, ignoreCase = true) } // Hiển thị gợi ý
            },
            label = { Text("Searching for...") }
        )

        ExposedDropdownMenu(
            expanded = isDropdownExpanded,
            onDismissRequest = { isDropdownExpanded = false }
        ) {
            searchHistory.filter { it.contains(query, ignoreCase = true) }
                .forEach { suggestion ->
                    DropdownMenuItem(
                        text = { Text(suggestion) },
                        onClick = {
                            query = suggestion
                            isDropdownExpanded = false
                            onSearch(suggestion) // 🔹 Gửi giá trị về MainScreen
                        }
                    )
                }
        }
    }
}

