package com.example.zd5_v8

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Client
import com.example.zd5_v8.database.Supplier
import kotlinx.coroutines.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var registerButton: Button
    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        roleSpinner = findViewById(R.id.roleSpinner)
        registerButton = findViewById(R.id.registerButton)
        nameEditText = findViewById(R.id.nameEditText)
        addressEditText = findViewById(R.id.addressEditText)

        val roles = arrayOf("Клиент", "Поставщик")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                emailEditText.text.clear()
                passwordEditText.text.clear()
                nameEditText.text.clear()
                addressEditText.text.clear()

                if (roles[position] == "Поставщик") {
                    addressEditText.visibility = View.VISIBLE
                } else {
                    addressEditText.visibility = View.GONE
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {

            }
        }

        emailEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null && !isValidEmail(text)) {
                emailEditText.error = "Неверный формат email"
            }
        }

        passwordEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null && text.length < 6) {
                passwordEditText.error = "Пароль должен быть не менее 6 символов"
            }
        }

        nameEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null && !startsWithCapital(text)) {
                nameEditText.error = "Имя должно начинаться с заглавной буквы"
            }
        }

        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val name = nameEditText.text.toString()
            val role = roleSpinner.selectedItem.toString()
            val address = addressEditText.text.toString()

            if (validateRegistration(email, password, name, role, address)) {
                saveToSharedPreferences(email, password, role)
                saveToDatabase(email, password, name, role, address)

                Toast.makeText(this@RegisterActivity, "Регистрация успешна", Toast.LENGTH_SHORT).show()

                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this@RegisterActivity, "Ошибка регистрации", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun startsWithCapital(name: CharSequence?): Boolean {
        if (name != null) {
            return name.matches(Regex("^[A-ZА-Я].*"))
        }
        return false
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun validateRegistration(email: String, password: String, name: String, role: String, address: String): Boolean {
        var isValid = false

        runBlocking {
            val db = AppDatabase.getDatabase(applicationContext)
            val existingClient = withContext(Dispatchers.IO) { db.clientDao().findClientByEmail(email) }
            val existingSupplier = withContext(Dispatchers.IO) { db.supplierDao().findSupplierByEmail(email) }

            if (existingClient == null && existingSupplier == null) {
                isValid = true
            } else{
                Toast.makeText(this@RegisterActivity, "Email уже используется", Toast.LENGTH_SHORT).show()
            }
            if (!startsWithCapital(name)) {
                runOnUiThread { Toast.makeText(this@RegisterActivity, "Имя должно начинаться с заглавной буквы", Toast.LENGTH_SHORT).show() }
                isValid = false
            }

            if (!isValidEmail(email) || password.length < 6 || !startsWithCapital(name) || name.isEmpty() || (role == "Поставщик" && address.isEmpty())) {
                isValid = false
            }

            if (!isValidEmail(email)) {
                isValid = false
            }

            if (password.length < 6) {
                isValid = false
            }
        }

        return isValid
    }

    private fun saveToSharedPreferences(email: String, password: String, role: String) {
        val sharedPreferences = getSharedPreferences("RegistrationPrefs", MODE_PRIVATE)
        sharedPreferences.edit().apply {
            putString("Email", email)
            putString("Password", password)
            putString("Role", role)
            apply()
        }
    }

    private fun saveToDatabase(email: String, password: String, name: String, role: String, address: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)

            when (role) {
                "Клиент" -> {
                    val newClient = Client(email = email, password = password, name = name, discount = 0.0)
                    db.clientDao().insert(newClient)
                }
                "Поставщик" -> {
                    val newSupplier = Supplier(email = email, password = password, name = name, address = address)
                    db.supplierDao().insert(newSupplier)
                }
            }
        }
    }
}
