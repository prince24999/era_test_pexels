package com.vp.era_test_pexels

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.vp.era_test_pexels.ui.main.SharedNavigationBar
import com.vp.era_test_pexels.ui.main.SharedTopBar
import com.vp.era_test_pexels.ui.theme.EraTestPexelsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EraTestPexelsTheme {
                var selectedIndex by remember { mutableIntStateOf(0) }

                Scaffold(modifier = Modifier.fillMaxWidth(),
                    topBar = {
                        SharedTopBar("Era Photo Search")

                    },
                    bottomBar = {
                        SharedNavigationBar(
                            selectedIndex = selectedIndex,
                            onItemSelected = { index ->
                                selectedIndex = index

                            }
                        )
                    }
                ) { innerPadding ->
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        contentAlignment = Alignment.Center
                    ) {
                        //SingleButton(modifier = Modifier.padding(innerPadding))

                    }
                }
            }

        }
    }
}


