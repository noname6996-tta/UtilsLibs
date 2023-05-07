package com.ntduc.securityutils

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import android.util.Base64
import androidx.annotation.RequiresApi
import java.io.*
import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.GCMParameterSpec

@RequiresApi(Build.VERSION_CODES.M)
object AndroidEncryption {
  private var keyAlias = "DEFAULT_ALIAS"
  private val ANDROID_KEY_STORE = "AndroidKeyStore"
  private val FIXED_IV = "abcdefghijkl"                           //(Lớp mã hoá bổ sung - 12 ký tự)
  private val ALGORITHM = KeyProperties.KEY_ALGORITHM_AES         //Thuật toán mã hoá AES
  private val BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM           //Chế độ mã hoá GCM
  private val PADDING = KeyProperties.ENCRYPTION_PADDING_NONE     //Không có padding
  private val TRANSFORMATION = "$ALGORITHM/$BLOCK_MODE/$PADDING"
  
  private var keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
  
  fun setKeyAlias(keyAlias: String) {
    this.keyAlias = keyAlias
  }
  
  fun encrypt(text: String): String {
    try {
      return encryptString(text)
    } catch (e: Exception) {
      e.printStackTrace()
    }
    
    return ""
  }
  
  fun decrypt(text: String): String {
    try {
      val bytes: ByteArray? = decryptString(text)
      return bytes?.let { String(it, Charsets.UTF_8) }.toString()
    } catch (e: Exception) {
      e.printStackTrace()
    }
    
    return ""
  }
  
  private fun generateKey(): SecretKey {
    return KeyGenerator.getInstance(ALGORITHM, ANDROID_KEY_STORE).apply {
      init(
        KeyGenParameterSpec.Builder(
          keyAlias, KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        ).setBlockModes(BLOCK_MODE).setEncryptionPaddings(PADDING)
          .setRandomizedEncryptionRequired(false).build()
      )
    }.generateKey()
  }
  
  private fun getSecretKey(): SecretKey {
    val existingKey = keyStore.getEntry(keyAlias, null) as? KeyStore.SecretKeyEntry
    return existingKey?.secretKey ?: generateKey()
  }
  
  private fun encryptString(input: String): String {
    try {
      val encryptCipher = Cipher.getInstance(TRANSFORMATION)
      encryptCipher!!.init(
        Cipher.ENCRYPT_MODE, getSecretKey(), GCMParameterSpec(128, FIXED_IV.toByteArray())
      )
      val encodedBytes = encryptCipher.doFinal(input.toByteArray())
      return Base64.encodeToString(encodedBytes, Base64.DEFAULT)
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
      e.printStackTrace()
    } catch (e: InvalidKeyException) {
      e.printStackTrace()
    } catch (e: InvalidAlgorithmParameterException) {
      e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
      e.printStackTrace()
    } catch (e: BadPaddingException) {
      e.printStackTrace()
    } catch (e: Exception) {
      e.printStackTrace()
    }
    
    return ""
  }
  
  private fun decryptString(encrypted: String): ByteArray? {
    try {
      val decryptCipher = Cipher.getInstance(TRANSFORMATION)
      decryptCipher!!.init(
        Cipher.DECRYPT_MODE, getSecretKey(), GCMParameterSpec(128, FIXED_IV.toByteArray())
      )
      val barr = Base64.decode(encrypted, Base64.DEFAULT)
      return decryptCipher.doFinal(barr)
    } catch (e: NoSuchAlgorithmException) {
      e.printStackTrace()
    } catch (e: NoSuchPaddingException) {
      e.printStackTrace()
    } catch (e: InvalidKeyException) {
      e.printStackTrace()
    } catch (e: InvalidAlgorithmParameterException) {
      e.printStackTrace()
    } catch (e: IllegalBlockSizeException) {
      e.printStackTrace()
    } catch (e: BadPaddingException) {
      e.printStackTrace()
    } catch (e: Exception) {
      e.printStackTrace()
    }
    
    return null
  }
}