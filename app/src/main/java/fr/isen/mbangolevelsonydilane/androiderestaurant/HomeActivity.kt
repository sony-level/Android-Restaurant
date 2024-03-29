package fr.isen.mbangolevelsonydilane.androiderestaurant

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import android.widget.Toast
import androidx.compose.material3.TextButton
import android.util.Log
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf

import fr.isen.mbangolevelsonydilane.androiderestaurant.ui.theme.AndroidERestaurantTheme

enum class DishType{
    STARTER,MAIN,DESSERT;
    @Composable
    fun title(): String {
        return when(this) {
            STARTER -> stringResource(id = R.string.menu_starter)
            MAIN -> stringResource(id = R.string.menu_main)
            DESSERT -> stringResource(id = R.string.menu_dessert)
        }
    }
}

interface AppUi{
    fun Display(dishType:DishType)
}
class HomeActivity : ComponentActivity(),AppUi {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidERestaurantTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting(this)
                }
            }
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        Log.d("HomeActivity", "L'activité Home est détruite")
    }
    override fun Display(dishType: DishType) {
        val intent = Intent(this, CategoryActivity::class.java)
        intent.putExtra(CategoryActivity.CATEGORY_EXTRA_KEY,dishType)
        startActivity(intent)
        Toast.makeText(this, "Voici mon toast", Toast.LENGTH_LONG).show()
    }

}

@Composable
fun Greeting(menu:AppUi) {
    val isHeaderFixed = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .padding(0.dp)
    ) {
        Header()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(70.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Bienvenue",
                    color = Color.Blue,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(modifier = Modifier.width(16.dp))
                Image(
                    painter = painterResource(id = R.drawable.restaurant),
                    contentDescription = null,
                    modifier = Modifier.size(120.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(type = DishType.STARTER, menu)
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Red, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(type = DishType.MAIN, menu)
        Spacer(modifier = Modifier.height(16.dp))
        Divider(color = Color.Red, thickness = 1.dp)
        Spacer(modifier = Modifier.height(16.dp))
        CustomButton(type = DishType.DESSERT, menu)
    }
}

@Composable fun CustomButton(type: DishType, menu: AppUi) {
    TextButton(onClick = { menu.Display(type)  }) {
        Text(type.title())
    }
}
@Composable
fun Header() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(Color.Blue)
    ) {
        // Contenu du header qui reste fixe
        Text(
            text = "Welcome to my Restaurant",
            modifier = Modifier
                .align(Alignment.Center),
            color = Color.White  // Couleur du texte du header
        )
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AndroidERestaurantTheme {
        Greeting(HomeActivity())
    }
}
