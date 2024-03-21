package fr.isen.mbangolevelsonydilane.androiderestaurant.Basket



import android.content.Context
import fr.isen.mbangolevelsonydilane.androiderestaurant.models.Dish
import com.google.gson.GsonBuilder

class Basket {
    var items: MutableList< BItems> = mutableListOf()
    fun add(dish: Dish, count: Int, context: Context) {
        val existingItem = items.firstOrNull { it.dish == dish }
        existingItem?.let {
            it.count = it.count + count
        } ?: run {
            items.add( BItems(count, dish))
        }
        save(context)
    }

    fun delete(item:  BItems, context: Context) {
        items.removeAll { item.dish.name == it.dish.name }
        save(context)
    }

    fun save(context: Context) {
        val json = GsonBuilder().create().toJson(this)

        val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString(BASKET_PREFERENCES_KEY, json)
        editor.apply()
    }
    companion object {
        fun current(context: Context): Basket {
            val sharedPreferences = context.getSharedPreferences(USER_PREFERENCES_NAME, Context.MODE_PRIVATE)
            val json = sharedPreferences.getString(BASKET_PREFERENCES_KEY, null)
            if(json != null) {
                return GsonBuilder().create().fromJson(json, Basket::class.java)
            }
            return Basket()
        }

        val USER_PREFERENCES_NAME = "USER_PREFERENCES_NAME"
        val BASKET_PREFERENCES_KEY = "BASKET_PREFERENCES_KEY"
    }
}