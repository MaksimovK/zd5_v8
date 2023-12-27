package com.example.zd5_v8

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Client
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditClientActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var btnSave: Button
    private var clientId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_client)


        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        nameEditText = findViewById(R.id.nameEditText)
        btnSave = findViewById(R.id.btnSave)


        emailEditText.doOnTextChanged { text, _, _, _ ->
            if (text != null && !isValidEmail(text.toString())) {
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


        clientId = intent.getIntExtra("CLIENT_ID", -1)
        if (clientId != -1) {
            loadClientData(clientId)

            btnSave.setOnClickListener {
                updateClientData()
            }
        } else {
            Toast.makeText(this, "Ошибка загрузки данных клиента!", Toast.LENGTH_LONG).show()
            finish()
        }
    }

    private fun isValidEmail(email: CharSequence?): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun startsWithCapital(name: CharSequence?): Boolean {
        if (name != null) {
            return name.matches(Regex("^[A-ZА-Я].*"))
        }
        return false
    }

    private fun loadClientData(clientId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val client = db.clientDao().getClientById(clientId)
            withContext(Dispatchers.Main) {
                client?.let {
                    emailEditText.setText(it.email)
                    passwordEditText.setText(it.password)
                    nameEditText.setText(it.name)
                }
            }
        }
    }

    private fun updateClientData() {
        val email = emailEditText.text.toString()
        val password = passwordEditText.text.toString()
        val name = nameEditText.text.toString()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            db.clientDao().update(Client(clientId, email, password, "Клиент", name, 0.0))
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Данные клиента обновлены", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}
