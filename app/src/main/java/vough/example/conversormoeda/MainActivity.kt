package vough.example.conversormoeda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.TextView
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class MainActivity : AppCompatActivity() {

    private lateinit var result: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        result = findViewById<TextView>(R.id.txt_result)

        val buttonConverter = findViewById<Button>(R.id.btn_converter)

        buttonConverter.setOnClickListener{
            converter()
        }
    }

    private fun converter()
    {
        val selectedCurrency = findViewById<RadioGroup>(R.id.radio_group)

        val checked = selectedCurrency.checkedRadioButtonId

        // igual ao if...else   verifica butao selecionado
        val currency = when(checked)
        {
            R.id.radio_usd -> "USD"
            else -> "CLP"
        }

        val editField = findViewById<EditText>(R.id.edit_field)

        val value = editField.text.toString()

        if (value.isEmpty()) return

        Thread {
            val url =
                URL("https://free.currconv.com/api/v7/convert?q=${currency}_EUR&compact=ultra&apiKey=65c11d347037d21506b2")

            val conn = url.openConnection() as HttpsURLConnection

            try {
                val data = conn.inputStream.bufferedReader().readText()

                val obj = JSONObject(data)

                runOnUiThread{
                    val res = obj.getDouble("${currency}_EUR")
                    result.text = "€EUR ${"%.4f".format(value.toDouble() * res)}"
                    result.visibility = View.VISIBLE
                }
            }
            finally {
                conn.disconnect()
            }
        }.start()

    }

}