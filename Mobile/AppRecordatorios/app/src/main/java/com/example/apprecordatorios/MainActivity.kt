package com.example.apprecordatorios


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONException
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var etUsuario: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button


    private val urlLogin = "http://10.0.2.2/app_recordatorios/login.php"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        etUsuario = findViewById(R.id.etUsuario)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)

        btnLogin.setOnClickListener {
            val usuario = etUsuario.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (usuario.isNotEmpty() && password.isNotEmpty()) {
                validarLogin(usuario, password)
            } else {
                Toast.makeText(this, "Por favor llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validarLogin(usuario: String, password: String) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, urlLogin,
            Response.Listener { response ->
                try {
                    val jsonObject = JSONObject(response)
                    val status = jsonObject.getString("status")
                    val message = jsonObject.getString("message")

                    if (status == "success") {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                        // Aquí agregaremos la navegación a la pantalla de Inicio en el siguiente paso
                        // val intent = Intent(this, InicioActivity::class.java)
                        // startActivity(intent)
                    } else {
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    Toast.makeText(this, "Error leyendo los datos", Toast.LENGTH_SHORT).show()
                }
            },
            Response.ErrorListener { error ->
                Toast.makeText(this, "Error de conexión. Verifica tu IP y XAMPP.", Toast.LENGTH_SHORT).show()
            }) {
            override fun getParams(): Map<String, String> {
                val parametros = HashMap<String, String>()
                parametros["usuario"] = usuario
                parametros["password"] = password
                return parametros
            }
        }

        val requestQueue = Volley.newRequestQueue(this)
        requestQueue.add(stringRequest)
    }
}