package com.example.zd5_v8

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Client
import com.example.zd5_v8.database.Supplier
import com.example.zd5_v8.database.Worker
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var roleSpinner: Spinner
    private lateinit var loginButton: Button
    private lateinit var registerButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        roleSpinner = findViewById(R.id.roleSpinner)
        loginButton = findViewById(R.id.loginButton)
        registerButton = findViewById(R.id.registerButton)

        val roles = arrayOf("Работник", "Клиент", "Поставщик")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)
        roleSpinner.adapter = adapter
        roleSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                if (view != null) {
                    emailEditText.text.clear()
                    passwordEditText.text.clear()
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

        val worker = Worker(email = "worker@mail.ru", password = "worker", name = "Евгений")
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.workerDao().insert(worker)
        }


        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val role = roleSpinner.selectedItem.toString()
            if (validateLogin(email, password)) {
                checkCredentials(email, password, role)
            }else {
                Toast.makeText(this@LoginActivity, "Неверный ввод. Проверьте данные.", Toast.LENGTH_SHORT).show()
            }
        }

        registerButton.setOnClickListener{
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }
    }
    private fun validateLogin(email: String, password: String): Boolean {
        if (!isValidEmail(email)) {
            return false
        }

        if (password.length < 6) {
            return false
        }

        return true
    }

    private fun checkCredentials(email: String, password: String, role: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)

            when (role) {
                "Работник" -> {
                    val worker = db.workerDao().findWorkerByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (worker != null && worker.password == password) {
                            navigateToWorkerActivity(worker)
                        } else {
                            Toast.makeText(this@LoginActivity, "Неверный ввод для Работника. Проверьте данные.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                "Клиент" -> {
                    val client = db.clientDao().findClientByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (client != null && client.password == password) {
                            navigateToClientActivity(client)
                        } else {
                            Toast.makeText(this@LoginActivity, "Неверный ввод для Клиента. Проверьте данные.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                "Поставщик" -> {
                    val supplier = db.supplierDao().findSupplierByEmail(email)
                    withContext(Dispatchers.Main) {
                        if (supplier != null && supplier.password == password) {
                            navigateToSupplierActivity(supplier)
                        } else {
                            Toast.makeText(this@LoginActivity, "Неверный ввод для Поставщика. Проверьте данные.", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                else -> {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@LoginActivity, "Неизвестная роль: $role", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun navigateToWorkerActivity(worker: Worker) {
        val intent = Intent(this@LoginActivity, WorkerActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToClientActivity(client: Client) {
        val intent = Intent(this@LoginActivity, ClientActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToSupplierActivity(supplier: Supplier) {
        val intent = Intent(this@LoginActivity, SupplierActivity::class.java)
        startActivity(intent)
    }

    fun isValidEmail(email: CharSequence?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}