package com.example.zd5_v8

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import com.example.zd5_v8.database.AppDatabase
import com.example.zd5_v8.database.Supplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EditSupplierActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var nameEditText: EditText
    private lateinit var addressEditText: EditText
    private lateinit var btnSave: Button
    private var supplierId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_supplier)

        supplierId = intent.getIntExtra("SUPPLIER_ID", -1)
        if (supplierId == -1) {
            Toast.makeText(this, "Ошибка загрузки данных поставщика!", Toast.LENGTH_LONG).show()
            finish()
        }

        emailEditText = findViewById(R.id.etSupplierEmail)
        passwordEditText = findViewById(R.id.etSupplierPassword)
        nameEditText = findViewById(R.id.etSupplierName)
        addressEditText = findViewById(R.id.etSupplierAddress)
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

        loadSupplierData(supplierId)

        btnSave.setOnClickListener {
            updateSupplierData()
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

    override fun onResume() {
        super.onResume()
        loadSupplierData(supplierId)
    }

    private fun loadSupplierData(supplierId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val supplier = db.supplierDao().getSupplierById(supplierId)
            withContext(Dispatchers.Main) {
                supplier?.let {
                    emailEditText.setText(it.email)
                    passwordEditText.setText(it.password)
                    nameEditText.setText(it.name)
                    addressEditText.setText(it.address)
                }
            }
        }
    }

    private fun updateSupplierData() {
        val email = emailEditText.text.toString().trim()
        val password = passwordEditText.text.toString().trim()
        val name = nameEditText.text.toString().trim()
        val address = addressEditText.text.toString().trim()

        if (email.isEmpty() || password.isEmpty() || name.isEmpty() || address.isEmpty()) {
            Toast.makeText(this, "Пожалуйста заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        CoroutineScope(Dispatchers.IO).launch {
            val db = AppDatabase.getDatabase(applicationContext)
            val updatedSupplier = Supplier(
                supplierId = supplierId,
                email = email,
                password = password,
                name = name,
                address = address
            )
            db.supplierDao().update(updatedSupplier)
            withContext(Dispatchers.Main) {
                Toast.makeText(applicationContext, "Данные поставщика обновлены", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

}
