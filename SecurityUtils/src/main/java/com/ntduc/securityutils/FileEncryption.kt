package com.ntduc.securityutils

import android.os.Build
import android.security.keystore.KeyProperties
import androidx.annotation.RequiresApi
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.security.InvalidKeyException
import java.security.NoSuchAlgorithmException
import javax.crypto.Cipher
import javax.crypto.CipherOutputStream
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

@RequiresApi(Build.VERSION_CODES.M)
object FileEncryption {
  private val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
  private val BLOCK_MODE = KeyProperties.BLOCK_MODE_CBC
  private val PADDING = "PKCS5padding"
  private val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
  private val BUFFER_SIZE = 1024
  
  @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, IOException::class)
  fun encryptToFile(
    secretKey: String,
    cypherSpecString: String,
    input: InputStream,
    output: OutputStream
  ) {
    var out = output
    try {
      val iv = IvParameterSpec(cypherSpecString.toByteArray(Charsets.UTF_8))
      val keyspec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGORITHM)
      
      val cipher = Cipher.getInstance(TRANSFORMATION)
      cipher.init(Cipher.ENCRYPT_MODE, keyspec, iv)
      out = CipherOutputStream(output, cipher)
      
      val buffer = ByteArray(BUFFER_SIZE)
      
      var bytesRead: Int
      while (input.read(buffer).also { bytesRead = it } > 0) {
        out.write(buffer, 0, bytesRead)
      }
    } finally {
      out.close()
    }
  }
  
  
  @Throws(NoSuchAlgorithmException::class, InvalidKeyException::class, IOException::class)
  fun decryptToFile(
    secretKey: String,
    cypherSpecString: String,
    input: InputStream,
    output: OutputStream
  ) {
    var out = output
    try {
      val iv = IvParameterSpec(cypherSpecString.toByteArray(Charsets.UTF_8))
      val keyspec = SecretKeySpec(secretKey.toByteArray(Charsets.UTF_8), ALGORITHM)
      
      val cipher = Cipher.getInstance(TRANSFORMATION)
      cipher.init(Cipher.DECRYPT_MODE, keyspec, iv)
      out = CipherOutputStream(output, cipher)
      
      val buffer = ByteArray(BUFFER_SIZE)
      
      var bytesRead: Int
      while (input.read(buffer).also { bytesRead = it } > 0) {
        out.write(buffer, 0, bytesRead)
      }
    } finally {
      out.close()
    }
  }
}