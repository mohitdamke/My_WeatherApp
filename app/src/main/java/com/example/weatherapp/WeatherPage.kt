package com.example.weatherapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.internal.composableLambdaInstance
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.W300
import androidx.compose.ui.text.font.FontWeight.Companion.W400
import androidx.compose.ui.text.font.FontWeight.Companion.W700
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.weatherapp.api.NetworkResponse
import com.example.weatherapp.api.WeatherModel

@Composable
fun WeatherPage(viewModel: WeatherViewModel) {

    val weatherResult = viewModel.weatherResult.observeAsState()
    var inputCity by rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Weather App", fontSize = 26.sp, fontWeight = W700)
        Row(
            modifier = Modifier.padding(10.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputCity,
                onValueChange = { inputCity = it },
                label = { Text(text = "Type the City Name", fontSize = 20.sp) },
                modifier = Modifier.weight(1f)
            )
            Spacer(modifier = Modifier.padding(5.dp))

            IconButton(onClick = {
                viewModel.getData(inputCity)
                keyboardController?.hide()
            }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        when (val result = weatherResult.value) {
            is NetworkResponse.failed -> {
                Text(text = result.message, fontSize = 20.sp, fontWeight = W300)
            }

            NetworkResponse.loading -> {
                CircularProgressIndicator()
            }

            is NetworkResponse.success -> {
                WeatherDetails(data = result.data)
            }

            null -> {}
        }

    }
}


@Composable
fun WeatherDetails(data: WeatherModel) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location icon",
                        modifier = Modifier.size(40.dp)
                    )
                    Text(text = data.location.name, fontSize = 30.sp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = data.location.country, fontSize = 18.sp, color = Gray)
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = " ${data.current.temp_c} ° c",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )

                AsyncImage(
                    modifier = Modifier.size(160.dp),
                    model = "https:${data.current.condition.icon}".replace("64x64", "128x128"),
                    contentDescription = "Condition icon"
                )
                Text(
                    text = data.current.condition.text,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    color = Gray
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("Humidity", data.current.humidity)
                            WeatherKeyVal("Wind Speed", data.current.wind_kph + " km/h")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("UV", data.current.uv)
                            WeatherKeyVal("Participation", data.current.precip_mm + " mm")
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            WeatherKeyVal("Local Time", data.location.localtime.split(" ")[1])
                            WeatherKeyVal("Local Date", data.location.localtime.split(" ")[0])
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WeatherKeyVal(key: String, value: String) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = value, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = key, fontWeight = FontWeight.SemiBold, color = Gray)
    }
}