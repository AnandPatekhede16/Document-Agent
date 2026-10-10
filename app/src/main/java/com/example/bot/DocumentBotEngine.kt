package com.example.bot

import com.example.data.model.ChatMessage
import com.example.data.model.MessageSender
import com.example.data.model.ServiceItem
import com.example.data.repository.ServiceRepository

object DocumentBotEngine {

  val defaultQuickPrompts = listOf(
    "Documents for College Admission",
    "Documents for Bonafide Certificate",
    "Documents for Transfer Certificate (TC)",
    "Documents for Caste Validity",
    "Documents for Gap Certificate",
    "What documents do I need for Passport?"
  )

  fun getInitialGreeting(): ChatMessage {
    return ChatMessage(
      sender = MessageSender.BOT,
      text = "Hello! I am your Document Requirement Bot 🤖\n\nI can answer questions regarding **educational documents** (Bonafide, TC, Migration, Caste Validity, Gap Affidavit, Marksheets, Scholarships) as well as **public & government services** (Passport, PAN, Aadhaar, Driving Licence, etc.).\n\nYou can ask me:\n• \"What documents are needed for college admission?\"\n• \"How to get Bonafide certificate?\"\n• \"What is a Gap certificate?\"\n• \"Documents for Caste Validity\"\n• \"What counts as address proof?\"\n\nHow can I help you today?",
      quickActions = defaultQuickPrompts
    )
  }

  fun processQuestion(userQuestion: String): ChatMessage {
    val q = userQuestion.trim().lowercase()

    // =========================================================================
    // 1. Comprehensive Educational Document Handlers
    // =========================================================================

    // A. Bonafide Certificate
    if (q.contains("bonafide") || q.contains("bonafide certificate")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🎓 **Bonafide Certificate Requirements & Process:**\n\n" +
          "**Purpose:** Proves that you are an enrolled, regular student of a specific school, college, or university during an academic year.\n\n" +
          "**Common Uses:**\n" +
          "• Applying for state/national scholarships (NSP, MahaDBT)\n" +
          "• Student bus/metro/train travel concessions\n" +
          "• Opening student bank accounts\n" +
          "• Passport applications for students\n" +
          "• Applying for education loans\n\n" +
          "📄 **Documents Needed to Apply at College/School Office:**\n" +
          "1. Current Academic Year College Fee Receipt / Admission Challan\n" +
          "2. Student Identity Card (ID Card copy)\n" +
          "3. Duly filled Bonafide Application Form (available at administrative office)\n" +
          "4. Previous year's passed marksheet copy\n\n" +
          "⏱️ **Processing Time:** Usually 1 to 3 working days from college administration.",
        quickActions = listOf("Documents for College Admission", "Documents for Scholarship Application", "Documents for Transfer Certificate (TC)")
      )
    }

    // B. Transfer Certificate (TC) / School Leaving Certificate (LC)
    if (q.contains("transfer certificate") || q.contains("tc") || q.contains("leaving certificate") || q.contains("school leaving") || q.contains("college leaving")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🏫 **Transfer Certificate (TC) / School Leaving Certificate (LC):**\n\n" +
          "**Purpose:** Certifies that a student has completed studies or formally withdrawn from an institution to join another school or college.\n\n" +
          "📄 **Documents & Clearances Required to Obtain TC/LC:**\n" +
          "1. **No Dues / Clearance Form:** Signed by Department, Central Library (no books pending), Sports Dept, and Accounts (all fees cleared)\n" +
          "2. Original Identity Card (surrendered at office)\n" +
          "3. Copy of Latest Passed Marksheet\n" +
          "4. Written application addressed to Principal/Registrar stating reason for transfer\n\n" +
          "⚠️ **Important Notes:**\n" +
          "• An institution issues only **ONE original TC/LC**. If lost, a police FIR and a notarized indemnity bond are required for a duplicate.\n" +
          "• For admission into any new college or university, the **Original TC/LC** is mandatory.",
        quickActions = listOf("Documents for Migration Certificate", "Documents for Bonafide Certificate", "Documents for Gap Certificate")
      )
    }

    // C. Migration Certificate
    if (q.contains("migration") || q.contains("migration certificate")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "✈️ **Migration Certificate Requirements:**\n\n" +
          "**Purpose:** Issued by an Education Board (CBSE, ICSE, State Board) or University when moving from one board/university to another.\n\n" +
          "**Difference between TC and Migration:**\n" +
          "• **TC (Transfer Certificate):** Issued by the *school/college*.\n" +
          "• **Migration Certificate:** Issued by the *central Board or University*.\n\n" +
          "📄 **Required Documents to Apply:**\n" +
          "1. College/School Leaving Certificate (TC copy)\n" +
          "2. Self-attested copies of all Semester / Board Marksheets\n" +
          "3. Passing Certificate or Degree Certificate copy\n" +
          "4. Prescribed Application Form of the University/Board\n" +
          "5. University Challan / Online Fee Receipt (₹200 - ₹500)\n\n" +
          "💡 *Tip: For CBSE, Migration Certificates are available instantly digitally on DigiLocker.*",
        quickActions = listOf("Documents for Transfer Certificate (TC)", "Documents for College Admission", "Documents for Degree Certificate")
      )
    }

    // D. Caste Validity Certificate
    if (q.contains("caste validity") || q.contains("validity certificate") || q.contains("scrutiny committee")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🛡️ **Caste Validity Certificate Requirements:**\n\n" +
          "**Purpose:** Proves the constitutional authenticity of your Caste Certificate. Mandatory for admission into Engineering, Medical, Pharmacy, MBA, and government jobs under reservation categories in India.\n\n" +
          "📄 **Mandatory Documents Required:**\n" +
          "1. Original Caste Certificate of Applicant\n" +
          "2. School Leaving Certificate (LC/TC) of Applicant showing caste and sub-caste\n" +
          "3. School Leaving Certificate of Father showing caste\n" +
          "4. Pre-constitutional genealogical proof (School records, Birth/Death extracts, or Revenue records of grandfather/paternal ancestors prior to 1950 for SC/ST or 1967 for OBC/VJNT)\n" +
          "5. Form 3/Form 16 Application Recommendation from College Principal (for admission)\n" +
          "6. Affidavit in prescribed Form 3 on ₹100 stamp paper\n" +
          "7. Detailed Family Tree (Vamshavali) notarized affidavit\n\n" +
          "⏱️ **Process:** Submitted to the Divisional Caste Scrutiny Committee (CCVIS portal). Takes 30 to 90 days.",
        quickActions = listOf("Documents for Caste Certificate", "Documents for Non-Creamy Layer (NCL)", "Documents for College Admission")
      )
    }

    // E. Gap Certificate / Gap Affidavit
    if (q.contains("gap certificate") || q.contains("gap affidavit") || q.contains("academic gap") || q.contains("drop year")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "⏳ **Gap Certificate (Academic Gap Affidavit):**\n\n" +
          "**Purpose:** Explains an educational break (e.g., 1 or 2 years gap between 12th & Degree or Degree & Master's) for competitive exam prep (JEE/NEET/UPSC), medical recovery, or personal reasons.\n\n" +
          "📄 **How to Get a Gap Certificate:**\n" +
          "1. A Gap Certificate is NOT issued by a college; it is a **Legal Notarized Affidavit**.\n" +
          "2. Purchase Non-Judicial Stamp Paper (typically ₹100 denomination) from local court / sub-registrar.\n" +
          "3. State clearly: Period of gap (e.g., June 2024 to June 2025), reason (Entrance preparation, illness), and declaration of no criminal/unlawful activities during this period.\n" +
          "4. Get it signed and notarized by a licensed Notary Advocate with notary seal.\n\n" +
          "📄 **Supporting Documents for Notary:**\n" +
          "• Aadhaar Card\n" +
          "• Last attended college TC / LC\n" +
          "• Marksheet of last passed examination",
        quickActions = listOf("Documents for College Admission", "Documents for Transfer Certificate (TC)", "Documents for Domicile Certificate")
      )
    }

    // F. Non-Creamy Layer (NCL)
    if (q.contains("non creamy layer") || q.contains("ncl") || q.contains("creamy layer")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "📑 **Non-Creamy Layer Certificate (NCL):**\n\n" +
          "**Purpose:** Mandatory for OBC, VJNT, and SBC category candidates to claim reservation benefits in college admissions and government recruitments.\n\n" +
          "🎯 **Eligibility:**\n" +
          "• Gross parental annual income from all sources must be **below ₹8 Lakhs** per annum for the preceding 3 consecutive financial years.\n\n" +
          "📄 **Required Documents:**\n" +
          "1. Original Caste Certificate of applicant\n" +
          "2. Last 3 years Income Certificate / Form 16 / ITR of parents\n" +
          "3. Applicant's School Leaving Certificate (TC)\n" +
          "4. Father's / Guardian's School Leaving Certificate\n" +
          "5. Ration Card copy showing family members\n" +
          "6. Electricity Bill or Address Proof\n" +
          "7. Self-declaration on ₹100 stamp paper (where applicable)\n\n" +
          "📅 **Validity:** Usually issued with validity of 3 financial years.",
        quickActions = listOf("Documents for Income Certificate", "Documents for Caste Validity", "Documents for College Admission")
      )
    }

    // G. General College & Higher Education Admission Documents
    if (q.contains("college admission") || q.contains("education documents") || q.contains("school admission") || q.contains("admission documents") || q.contains("university admission")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🎓 **Standard Checklist for College & University Admissions in India:**\n\n" +
          "**A. Mandatory Academic Records:**\n" +
          "1. 10th (SSC) Marksheet & Board Passing Certificate\n" +
          "2. 12th (HSC) Marksheet & Board Passing Certificate\n" +
          "3. College Leaving Certificate (LC / TC) (Original required)\n" +
          "4. Migration Certificate (if coming from CBSE/ICSE or other university)\n" +
          "5. Entrance Exam Score Card (JEE, NEET, MHT-CET, CUET, GATE, CAT, etc.)\n" +
          "6. Seat Allotment Letter / CAP Round Allotment Letter\n\n" +
          "**B. Identity & Domicile Proofs:**\n" +
          "7. Aadhaar Card (with correct DoB & active mobile linking)\n" +
          "8. Domicile & Nationality Certificate\n" +
          "9. Recent Passport-size Photographs (8 to 10 copies with white background)\n\n" +
          "**C. If Applying Under Reserved / Quota Categories:**\n" +
          "10. Caste Certificate & Caste Validity Certificate\n" +
          "11. Non-Creamy Layer Certificate (for OBC/VJNT/SBC valid for current year)\n" +
          "12. EWS / Income Certificate (if claiming EWS quota or fee concession)\n" +
          "13. Gap Certificate Affidavit (if having 1+ years study gap)",
        quickActions = listOf("Documents for Bonafide Certificate", "Documents for Caste Validity", "Documents for Gap Certificate", "Documents for Scholarship Application")
      )
    }

    // H. Education Loan Documents
    if (q.contains("education loan") || q.contains("loan") || q.contains("study loan")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🏦 **Documents Required for Education Loans (Bank Checklist):**\n\n" +
          "**A. Student Documents:**\n" +
          "• Admission Offer Letter from College/University with Course Fee Structure\n" +
          "• Marksheets of 10th, 12th, and Graduation (if PG course)\n" +
          "• Entrance Exam Scorecard (JEE, NEET, GRE, GMAT, IELTS, etc.)\n" +
          "• Proof of Identity: Aadhaar Card, PAN Card, Passport\n" +
          "• Proof of Residence: Electricity Bill, Rent Agreement, Passbook\n" +
          "• Passport-size Photographs (3 copies)\n\n" +
          "**B. Co-Borrower / Parent Documents (Mandatory):**\n" +
          "• PAN Card & Aadhaar Card of parent/guarantor\n" +
          "• Last 6 months bank account statements\n" +
          "• Last 2 years Income Tax Returns (ITR) or Form 16\n" +
          "• Salary Slips (last 3 months) or Business Registration proof\n" +
          "• Collateral property documents (if loan amount exceeds ₹7.5 Lakhs)",
        quickActions = listOf("Documents for College Admission", "Documents for PAN Card", "Documents for Income Certificate")
      )
    }

    // I. 10th & 12th Marksheets / Passing Certificate / DigiLocker
    if (q.contains("marksheet") || q.contains("passing certificate") || q.contains("10th marksheet") || q.contains("12th marksheet") || q.contains("board certificate")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "📜 **10th & 12th Marksheets vs. Passing Certificates:**\n\n" +
          "**Difference:**\n" +
          "• **Marksheet:** Contains marks obtained in individual subjects and GPA.\n" +
          "• **Passing Certificate:** Confirms successful completion of the secondary/higher secondary examination and serves as primary legal Proof of Age (DoB).\n\n" +
          "💡 **How to Get Official Verified Digital Copies:**\n" +
          "1. Open the government **DigiLocker** app or website (digilocker.gov.in).\n" +
          "2. Link your Aadhaar number.\n" +
          "3. Search for your board (CBSE, ICSE, Maharashtra State Board, UP Board, etc.).\n" +
          "4. Enter your Exam Year and Roll Number to fetch your digitally signed Marksheet & Passing Certificate.\n" +
          "5. Under Rule 9A of the IT Act, digital certificates from DigiLocker are legally equivalent to originals.",
        quickActions = listOf("Documents for College Admission", "Documents for Transfer Certificate (TC)", "Documents for Passport")
      )
    }

    // J. EWS Certificate (Economically Weaker Section)
    if (q.contains("ews") || q.contains("economically weaker")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "📋 **EWS Certificate (Economically Weaker Sections):**\n\n" +
          "**Purpose:** Provides 10% reservation in central/state admissions and government recruitments for general category candidates not covered under SC/ST/OBC.\n\n" +
          "🎯 **Criteria:**\n" +
          "• Family annual income below ₹8 Lakhs from all sources.\n" +
          "• Residential flat size below 1000 sq ft or residential plot below 100/200 sq yards in notified municipalities.\n\n" +
          "📄 **Required Documents:**\n" +
          "1. Aadhaar Card & PAN Card\n" +
          "2. Family Income Proof / ITR / Form 16 / Salary slips\n" +
          "3. Land and property documents (7/12 extract or property tax receipt)\n" +
          "4. Ration Card\n" +
          "5. Domicile Certificate\n" +
          "6. Self-declaration affidavit on stamp paper",
        quickActions = listOf("Documents for Income Certificate", "Documents for College Admission", "Documents for Domicile Certificate")
      )
    }

    // =========================================================================
    // 2. Standard Proof Concept Handlers
    // =========================================================================
    if (q.contains("address proof") || q.contains("proof of address") || q.contains("poa")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "📄 **Address Proof (Proof of Address):**\n\n" +
          "**Purpose:** Used to verify the applicant's current residential address.\n\n" +
          "**Accepted Examples:**\n" +
          "• Aadhaar Card\n" +
          "• Electricity / Water / Piped Gas Bill (less than 3 months old)\n" +
          "• Bank Account Statement / Passbook with photo\n" +
          "• Registered Rent Agreement\n" +
          "• Indian Passport\n" +
          "• Voter ID (EPIC Card)\n\n" +
          "💡 *Tip: Ensure the name spelling on your address proof matches your identity documents.*",
        quickActions = listOf("What is identity proof?", "Documents for Passport", "Documents for Aadhaar Services")
      )
    }

    if (q.contains("identity proof") || q.contains("proof of identity") || q.contains("poi")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🪪 **Identity Proof (Proof of Identity):**\n\n" +
          "**Purpose:** Used to legally authenticate the applicant's name, face photograph, and biological identity.\n\n" +
          "**Accepted Examples:**\n" +
          "• Aadhaar Card\n" +
          "• PAN Card\n" +
          "• Indian Passport\n" +
          "• Voter ID Card\n" +
          "• Driving Licence\n" +
          "• Government Employee ID\n\n" +
          "💡 *Tip: Make sure the photo is clear and the full legal name matches across all submitted forms.*",
        quickActions = listOf("What is address proof?", "Documents for PAN Card", "Documents for Voter ID")
      )
    }

    if (q.contains("date of birth") || q.contains("dob proof") || q.contains("age proof")) {
      return ChatMessage(
        sender = MessageSender.BOT,
        text = "🎂 **Date of Birth Proof (Proof of DoB / Age):**\n\n" +
          "**Purpose:** Confirms your exact legal birth date and age qualification.\n\n" +
          "**Accepted Examples:**\n" +
          "• Municipal / CRS Birth Certificate\n" +
          "• 10th / Matriculation School Passing Certificate\n" +
          "• PAN Card\n" +
          "• Passport\n" +
          "• Aadhaar Card (where full DD/MM/YYYY is verified)\n\n" +
          "💡 *Tip: Digital birth certificates from CRS are accepted across all official portals.*",
        quickActions = listOf("Birth Certificate documents", "Driving Licence requirements", "Passport documents")
      )
    }

    // =========================================================================
    // 3. Search Matching Service in Local Repository
    // =========================================================================
    val matchedService = findMatchingService(q)
    if (matchedService != null) {
      return generateServiceResponse(matchedService, q)
    }

    // =========================================================================
    // 4. Fallback Guidance
    // =========================================================================
    return ChatMessage(
      sender = MessageSender.BOT,
      text = "I couldn't find an exact match for your question in the offline database.\n\n" +
        "Try asking about:\n" +
        "• **Educational:** Bonafide, Transfer Certificate (TC), Migration, Caste Validity, Gap Affidavit, College Admission, Education Loan\n" +
        "• **Public Services:** Passport, PAN Card, Driving Licence, Voter ID, Income Certificate, Caste Certificate, Domicile, Birth Certificate, Ration Card\n\n" +
        "💡 *Tip: You can also configure your Gemini API Key in the Secrets Panel for comprehensive AI answers on any document question.*",
      quickActions = listOf(
        "Documents for College Admission",
        "Documents for Bonafide Certificate",
        "Documents for Passport",
        "Documents for PAN Card"
      )
    )
  }

  private fun findMatchingService(query: String): ServiceItem? {
    for (service in ServiceRepository.services) {
      if (query.contains(service.id.replace("_", " "))) return service
      if (query.contains(service.name.lowercase())) return service
      if (service.keywords.any { keyword -> query.contains(keyword.lowercase()) }) {
        return service
      }
    }
    return null
  }

  private fun generateServiceResponse(service: ServiceItem, query: String): ChatMessage {
    val isAskingEligibility = query.contains("eligible") || query.contains("eligibility") || query.contains("who can apply") || query.contains("age")
    val isAskingHowToApply = query.contains("apply") || query.contains("step") || query.contains("process") || query.contains("procedure") || query.contains("how to")

    val responseText = buildString {
      if (isAskingEligibility) {
        append("📋 **Eligibility Criteria for ${service.name}:**\n\n")
        service.eligibilityCriteria.forEach { crit ->
          append("• $crit\n")
        }
        append("\n💡 Tap below to view the full checklist and official portal link.")
      } else if (isAskingHowToApply) {
        append("📝 **How to Apply for ${service.name}:**\n\n")
        service.applicationSteps.take(5).forEach { step ->
          append("${step.stepNumber}. **${step.title}:** ${step.description}\n")
        }
        append("\n💡 Tap below for the complete checklist.")
      } else {
        append("📋 **Required Documents for ${service.name}:**\n\n")
        append("${service.shortDescription}\n\n")
        append("**Mandatory Proofs:**\n")
        service.requiredDocuments.take(5).forEach { doc ->
          append("• **${doc.name}:** ${doc.purpose}\n")
        }
        if (service.optionalDocuments.isNotEmpty()) {
          append("\n**Optional / Conditional:**\n")
          service.optionalDocuments.take(2).forEach { doc ->
            append("• ${doc.name} (${doc.purpose})\n")
          }
        }
        append("\n⚠️ *Note: Verify with the official department portal (${service.department}).*")
      }
    }

    return ChatMessage(
      sender = MessageSender.BOT,
      text = responseText,
      matchedServiceId = service.id,
      matchedServiceName = service.name,
      quickActions = listOf(
        "How to apply for ${service.name}?",
        "Eligibility for ${service.name}",
        "Important notes for ${service.name}"
      )
    )
  }
}
