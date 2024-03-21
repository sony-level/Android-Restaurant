package fr.isen.mbangolevelsonydilane.androiderestaurant





import android.app.DownloadManager.Request

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.gson.GsonBuilder
import fr.isen.mbangolevelsonydilane.androiderestaurant.models.Category
import fr.isen.mbangolevelsonydilane.androiderestaurant.models.Dish
import fr.isen.mbangolevelsonydilane.androiderestaurant.models.CategoryResults
import fr.isen.mbangolevelsonydilane.androiderestaurant.models.Shop
import org.json.JSONObject
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.verticalScroll

import androidx.compose.foundation.rememberScrollState

import fr.isen.mbangolevelsonydilane.androiderestaurant.ui.theme.AndroidERestaurantTheme


class CategoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val type = (intent.getSerializableExtra(CATEGORY_EXTRA_KEY)  as? DishType) ?: DishType.STARTER
        setContent {
            Homepage(type)
        }
        Log.d("lifeCycle", "Menu Activity - OnCreate")
    }

    override fun onPause() {
        Log.d("lifeCycle", "Menu Activity - OnPause")
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        Log.d("lifeCycle", "Menu Activity - OnResume")
    }

    override fun onDestroy() {
        Log.d("lifeCycle", "Menu Activity - onDestroy")
        super.onDestroy()
    }
    companion object{
        val CATEGORY_EXTRA_KEY ="CATEGORY_EXTRA_KEY"
    }
}

@Composable
fun Homepage(type: DishType) {
    Column (
        modifier = Modifier
            .padding(12.dp)
    ){
        Header()
    }
    Spacer(modifier = Modifier.height(156.dp))
    val category = remember {
        mutableStateOf<Category?>(null)
    }
    Spacer(modifier = Modifier.height(46.dp))
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(type.title())
        LazyColumn{
            category.value?.let{
                items(it.items){
                    dishRow(it)
                }

            }
        }
    }
    postData(type,category)
}


@Composable
fun dishRow(dish: Dish) {
    val context = LocalContext.current
    //LazyColumn {
    //items(listOf(dish)) {
    Card(
        modifier = Modifier
            .padding(top = 50.dp)
            .fillMaxWidth()
            .background(Color.Red)
            .clickable {
                val imageUri = dish.images.firstOrNull()
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.DISH_EXTRA_KEY, dish)
                intent.putExtra(DetailActivity.IMAGE_URI_EXTRA_KEY, imageUri)
                context.startActivity(intent)
            },
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, Color.White)
    ) {
        Column(
            modifier = Modifier.padding(bottom=16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {


                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(dish.images.firstOrNull())
                        .build(),
                    contentDescription = null,
                    placeholder = painterResource(R.drawable.restaurant),
                    error = painterResource(R.drawable.restaurant),
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp) // Hauteur fixe pour l'image
                        .clip(RoundedCornerShape(10))
                        .padding(end = 16.dp)
                )

            }
            // Conteneur pour le nom et le prix
            Column {
                // Nom du plat
                Text(
                    text = dish.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Red,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier.padding(bottom = 4.dp)
                )
                // Prix du plat
                Text(
                    "${dish.prices.firstOrNull()?.price} £",
                    style = MaterialTheme.typography.labelLarge
                )
            }
            // Espace pour étirer les éléments à gauche
            Spacer(modifier = Modifier.weight(1f))
        }
    }

}



@Composable
fun postData(type:DishType,category:MutableState<Category?>){
    val currentCategory = type.title()
    val context = LocalContext.current
    val queue = Volley.newRequestQueue(context)

    val params = JSONObject()
    params.put(Shop.id_shop,1)
    val request = JsonObjectRequest(
        com.android.volley.Request.Method.POST,
        Shop.URL,
        params,
        { response->
            Log.d("request",response.toString(2))
            val result= GsonBuilder().create().fromJson(response.toString(),CategoryResults::class.java)
            val filteredResult= result.data.first{category->
                category.name==currentCategory
            }
            category.value=filteredResult
            Log.d("parse","")
        },
        {
            Log.e("request",it.toString())
        }
    )
    queue.add(request)
}