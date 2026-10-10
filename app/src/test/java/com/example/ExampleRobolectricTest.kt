package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.bot.DocumentBotEngine
import com.example.data.model.MessageSender
import com.example.data.model.ServiceCategory
import com.example.data.repository.ServiceRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read app_name string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Document Requirement Bot", appName)
  }

  @Test
  fun `verify twelve sample services loaded`() {
    assertEquals(12, ServiceRepository.services.size)
  }

  @Test
  fun `verify service search functionality`() {
    val passportResults = ServiceRepository.searchServices("passport")
    assertTrue(passportResults.isNotEmpty())
    assertEquals("Passport", passportResults.first().name)

    val drivingResults = ServiceRepository.searchServices("driv")
    assertTrue(drivingResults.any { it.name == "Driving Licence" })

    val categoryResults = ServiceRepository.searchServices("", ServiceCategory.IDENTITY)
    assertTrue(categoryResults.isNotEmpty())
  }

  @Test
  fun `verify bot engine keyword recognition`() {
    val replyPassport = DocumentBotEngine.processQuestion("What documents are needed for passport?")
    assertEquals(MessageSender.BOT, replyPassport.sender)
    assertEquals("passport", replyPassport.matchedServiceId)
    assertTrue(replyPassport.text.contains("Required Documents for Passport"))

    val replyAddressProof = DocumentBotEngine.processQuestion("What is address proof?")
    assertTrue(replyAddressProof.text.contains("Proof of Address"))

    val replyUnknown = DocumentBotEngine.processQuestion("how to bake a cake")
    assertTrue(replyUnknown.text.contains("I couldn't identify the service"))
  }
}
