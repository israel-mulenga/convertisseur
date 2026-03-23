package com.example.convertisseur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TemperatureScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

// --- LOGIQUE MÉTIER (Fonctions Pures) ---
// Ces fonctions sont isolées de l'UI pour être testables
fun convertirCelsiusVersFahrenheit(celsius: Double): Double {
    return (celsius * 9 / 5) + 32
}

fun convertirFahrenheitVersCelsius(fahrenheit: Double): Double {
    return (fahrenheit - 32) * 5 / 9
}

// --- COMPOSANTS UI RÉUTILISABLES (Stateless) ---
@Composable
fun ChampTemperature(
    label: String,
    valeur: String,
    onValeurChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = valeur,
        onValueChange = { onValeurChange(it) }, // Utilisation efficace de 'it'
        label = { Text(label) },
        modifier = modifier.fillMaxWidth(),
        singleLine = true
    )
}

@Composable
fun ZoneResultat(message: String, resultat: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .background(Color.DarkGray),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = message, color = Color.LightGray, fontSize = 14.sp)
            Text(
                text = resultat,
                color = Color.White,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// --- ÉCRAN PRINCIPAL (State Hoisting) ---
@Composable
fun TemperatureScreen(modifier: Modifier = Modifier) {
    // Gestion de la mémoire via 'remember' (État hissé)
    var inputTemp by remember { mutableStateOf("") }
    var resultatStr by remember { mutableStateOf("0.0") }
    var uniteSelectionnee by remember { mutableStateOf("Celsius") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Convertisseur de Température",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Utilisation du composant réutilisable
        ChampTemperature(
            label = "Entrez la température en $uniteSelectionnee",
            valeur = inputTemp,
            onValeurChange = { inputTemp = it }
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Bouton Celsius -> Fahrenheit
            Button(
                onClick = {
                    uniteSelectionnee = "Celsius"
                    val temp = inputTemp.toDoubleOrNull() ?: 0.0
                    resultatStr = "${convertirCelsiusVersFahrenheit(temp)} °F"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Vers Fahrenheit")
            }

            // Bouton Fahrenheit -> Celsius
            Button(
                onClick = {
                    uniteSelectionnee = "Fahrenheit"
                    val temp = inputTemp.toDoubleOrNull() ?: 0.0
                    resultatStr = "${convertirFahrenheitVersCelsius(temp)} °C"
                },
                modifier = Modifier.weight(1f)
            ) {
                Text("Vers Celsius")
            }
        }

        // Affichage du résultat via le composant spécialisé
        ZoneResultat(
            message = "Résultat de la conversion :",
            resultat = resultatStr
        )
    }
}