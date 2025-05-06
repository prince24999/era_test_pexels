Search and show Photos on Pexels

Written in Kotlin and Jetpack Compose

Required to use (or build)
- Android Studio Meerkat | 2024.3.1 Patch 1
- Kotlin version : 2.0.21
- Dependencies :

    implementation ("androidx.datastore:datastore-preferences:1.0.0")

    implementation("com.google.accompanist:accompanist-swiperefresh:0.31.1-alpha")

    implementation ("androidx.compose.ui:ui:1.5.0")

    implementation ("androidx.compose.foundation:foundation:1.5.0")

    implementation ("androidx.compose.material:material:1.5.0")

    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")

    implementation ("androidx.activity:activity-compose:1.7.0")

    implementation ("com.squareup.picasso:picasso:2.8")

    implementation ("com.android.volley:volley:1.2.1")

    implementation ("com.google.code.gson:gson:2.8.9")

    implementation("io.coil-kt:coil-compose:2.4.0")


Core Functionality

● Users can search for images using the Pexels Search API.

● Search results will be displayed in a LazyVerticalGrid with "scroll to end to Load more".

● Use FAB (Floating Action Button) to Refresh (easier to use than pull-down-to-refresh).

● Clicking on an image should open a detail view of the image.

● Support for pinch-to-zoom and drag gestures

● Full Responsive/Adaptive layouts to support different screen sizes

● Support for search history or recent searches


![Search Form](https://photos.google.com/share/AF1QipM5mCLteF6DZAqXuow0R_NzdsZJ6RXeqQ1OaCw1uazHyCRZJuMTXvAa3Z7nOnPzWQ/photo/AF1QipP1ufy-kqXh-TDkSUgnQW1NELH3G-hLxLknRZrp?key=cmplTXBlSlVlSE9zLXVXRzRpQkZJNjhSWHBBNm9n)
![Photo List](https://photos.google.com/share/AF1QipMDd_gl0F_ZK4uDnOqbOaHfGdyyRY4Ye6jNW6JLG41Bctd9pdRduG1EXlpur8NoYg/photo/AF1QipNYl8venDNPU_gawH858EVOOoG2JzPMejg0UXhm?key=YTlqZDNoVmltYmN6MVhKTW9fN1FXbENqdl8tX01n)
![Photo Detail View](https://photos.google.com/share/AF1QipMD7yQcSB4a_jhQZfuuRTyR6zEWhp1Ah2rfu_pBhvZDy7IyQf4GuFaFBYY8lwCAOg/photo/AF1QipP_EuYagCeCvXtL4kHjbb0ykaGtxxb_Lx83AsCk?key=WFptV3YxeU5VSVV2TFVMQzVHY1Vjdk0xQnl5OF9R)
![Pinch to zoom view](https://photos.google.com/share/AF1QipM6EekVFORqjDOyH3Qws5BD0n5v--u-y581Y_8t4AO4dBTkPKNi93Plft3wLdwZ9A/photo/AF1QipOCJ6vNCGhpe7qLaKNEyB2CopsfIysJ4eM51Vm2?key=eENKRVFYT3ZzZi03OE1mRWVWVUlwNl81dHlLWWJn)

📄 License
MIT License.





