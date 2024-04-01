package ru.mirea.chmykhovam.firebaseauth

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ru.mirea.chmykhovam.firebaseauth.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName
    private lateinit var mAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.createAccountButton.setOnClickListener {
            createAccount(
                binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString()
            )
        }
        binding.signInButton.setOnClickListener {
            signIn(
                binding.editTextEmail.text.toString(), binding.editTextPassword.text.toString()
            )
        }
        binding.signOutButton.setOnClickListener { signOut() }
        binding.verifyEmailButton.setOnClickListener { sendEmailVerification() }
        // [START initialize_auth] Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance()
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) (in updateUI method) and update UI accordingly.
        val currentUser = mAuth.currentUser
        updateUI(currentUser)

    }
    // [END on_start_check_user]

    private fun updateUI(user: FirebaseUser?) {
        if (user != null) {
            binding.statusTextView.text = getString(
                R.string.emailpassword_status_fmt, user.email, user.isEmailVerified
            )
            binding.detailTextView.text = getString(R.string.firebase_status_fmt, user.uid)
            binding.emailPasswordButtons.visibility = View.GONE
            binding.emailPasswordFields.visibility = View.GONE
            binding.signedInButtons.visibility = View.VISIBLE
            binding.verifyEmailButton.setEnabled(!user.isEmailVerified)
        } else {
            binding.statusTextView.text = getString(R.string.signed_out)
            binding.detailTextView.text = null
            binding.emailPasswordButtons.visibility = View.VISIBLE
            binding.emailPasswordFields.visibility = View.VISIBLE
            binding.signedInButtons.visibility = View.GONE
        }
    }

    private fun createAccount(email: String, password: String) {
//        val email = binding.editTextEmail.text.toString()
//        val password = binding.editTextPassword.text.toString()
        Log.d(TAG, "createAccount:$email")
        if (!validateForm()) {
            return
        }
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "createUserWithEmail:success")
                val user = mAuth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(
                    TAG, "createUserWithEmail:failure", task.exception
                )
                Toast.makeText(this@MainActivity, "Authentication failed.", Toast.LENGTH_SHORT)
                    .show()
                updateUI(null)
            }
        }
        // [END create_user_with_email]
    }

    private fun validateForm(): Boolean {
        var valid = true
        val email = binding.editTextEmail.text.toString()
        val password = binding.editTextPassword.text.toString()

        if (TextUtils.isEmpty(email)) {
            binding.editTextEmail.error = "Required."
            valid = false
        } else {
            binding.editTextEmail.error = null
        }
        if (TextUtils.isEmpty(password)) {
            binding.editTextPassword.error = "Required."
            valid = false
        } else {
            binding.editTextPassword.error = null
        }
        return valid
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn:$email")
        if (!validateForm()) {
            return
        }
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(
            this
        ) { task ->
            if (task.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.d(TAG, "signInWithEmail:success")
                val user = mAuth.currentUser
                updateUI(user)
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(this@MainActivity, "Authenticationfailed.", Toast.LENGTH_SHORT)
                    .show()
                updateUI(null)
            }
            // [START_EXCLUDE]
            if (!task.isSuccessful) {
                binding.statusTextView.setText(R.string.auth_failed)
            }
            // [END_EXCLUDE]
        }
        // [END sign_in_with_email]
    }

    private fun signOut() {
        mAuth.signOut()
        updateUI(null)
    }

    private fun sendEmailVerification() {
        // Disable button
        binding.verifyEmailButton.setEnabled(false)
        // Send verification email
        // [START send_email_verification]
        val user = mAuth.currentUser ?: return
        user.sendEmailVerification().addOnCompleteListener(
            this
        ) { task ->
            // [START_EXCLUDE]
            // Re-enable button
            binding.verifyEmailButton.setEnabled(true)
            if (task.isSuccessful) {
                Toast.makeText(
                    this@MainActivity,
                    "Verification email sent to " + user.email,
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Log.e(TAG, "sendEmailVerification", task.exception)
                Toast.makeText(
                    this@MainActivity, "Failed to send verification email.", Toast.LENGTH_SHORT
                ).show()
            }

            // [END_EXCLUDE]
        }
        // [END send_email_verification]
    }
}