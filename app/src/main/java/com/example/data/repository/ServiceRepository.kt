package com.example.data.repository

import com.example.data.model.ApplicationStep
import com.example.data.model.DocumentItem
import com.example.data.model.FaqItem
import com.example.data.model.ServiceCategory
import com.example.data.model.ServiceItem
import com.example.data.model.StatePortalInfo

object ServiceRepository {

  val standardStatePortals = listOf(
    StatePortalInfo("Maharashtra", "Aaple Sarkar", "https://aaplesarkar.mahaonline.gov.in", "Requires MahaOnline registration with mobile OTP."),
    StatePortalInfo("Uttar Pradesh", "e-District UP", "https://edistrict.up.gov.in", "Certificates verified online via 16-digit application number."),
    StatePortalInfo("Karnataka", "Seva Sindhu", "https://sevasindhu.karnataka.gov.in", "Available via Bangalore One & Grama One service centers."),
    StatePortalInfo("Delhi", "e-District Delhi", "https://edistrict.delhigovt.nic.in", "Doorstep delivery of public services available in NCT."),
    StatePortalInfo("Tamil Nadu", "TNeGA e-Sevai", "https://www.tnesevai.tn.gov.in", "Citizen access through CAN registration."),
    StatePortalInfo("Bihar", "RTPS Bihar", "https://serviceonline.bihar.gov.in", "Direct application under Right to Public Services Act.")
  )

  val services: List<ServiceItem> = listOf(
    // 1. Aadhaar Services
    ServiceItem(
      id = "aadhaar",
      name = "Aadhaar Services",
      shortDescription = "Enrollment, update of demographic data, and biometric verification.",
      description = "Aadhaar is a 12-digit unique identity number issued by UIDAI to residents of India based on demographic and biometric data. Used across banking, subsidies, and identity verification.",
      category = ServiceCategory.IDENTITY,
      department = "Unique Identification Authority of India (UIDAI)",
      officialPortalUrl = "https://myaadhaar.uidai.gov.in",
      estimatedFee = "Free for new enrollment; ₹50 for demographic update; ₹100 for biometric update",
      processingTime = "15 to 90 days (Instant tracking via 28-digit EID)",
      applicationMode = "Online Appointment + Physical Visit to Kendra",
      requiredDocuments = listOf(
        DocumentItem(
          id = "aadh_poi",
          name = "Proof of Identity (PoI)",
          purpose = "To verify the applicant's legal name and photograph.",
          examples = listOf("Passport", "PAN Card", "Voter ID Card", "Driving Licence", "Ration Card with Photo", "Government Employee Photo ID"),
          required = true,
          notes = "The name on the PoI must match the application spelling exactly."
        ),
        DocumentItem(
          id = "aadh_poa",
          name = "Proof of Address (PoA)",
          purpose = "To verify the applicant's current residential address for communication.",
          examples = listOf("Electricity Bill (last 3 months)", "Water Bill (last 3 months)", "Bank Passbook/Statement", "Rent Agreement", "Passport", "Voter ID"),
          required = true,
          notes = "Utility bills must generally not be older than 3 months."
        ),
        DocumentItem(
          id = "aadh_dob",
          name = "Proof of Date of Birth (DoB)",
          purpose = "To verify the exact birth date for demographic records.",
          examples = listOf("Birth Certificate issued by Registrar/Municipal Authority", "SSLC / 10th Class Passing Certificate", "Passport", "PAN Card"),
          required = true,
          notes = "Only declared DoB certificates from recognized authorities are approved."
        ),
        DocumentItem(
          id = "aadh_por",
          name = "Proof of Relationship (PoR)",
          purpose = "Required for minors or Head-of-Family (HoF) based enrollment/updates.",
          examples = listOf("Birth Certificate", "Ration Card", "Marriage Certificate", "Pension Card"),
          required = false,
          notes = "Only mandatory if applying under Head of Family mode or for child under 5 years."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "aadh_hof",
          name = "Head of Family Consent Letter",
          purpose = "For family members lacking personal address proofs.",
          examples = listOf("Standard UIDAI HoF self-declaration form"),
          required = false,
          notes = "The HoF must accompany applicant to Aadhaar Seva Kendra."
        )
      ),
      eligibilityCriteria = listOf(
        "Any resident of India who has resided in the country for 182 days or more in the preceding 12 months.",
        "Non-Resident Indians (NRIs) holding valid Indian passport can also enroll.",
        "Children from birth can be enrolled (Baal Aadhaar for children under 5 years without biometrics).",
        "Mandatory biometric update at age 5 and age 15."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Locate Enrollment Center", "Find nearest Aadhaar Seva Kendra or authorized post office/bank branch via UIDAI portal."),
        ApplicationStep(2, "Book Appointment Online", "Reserve slot on myAadhaar portal to skip long physical queues."),
        ApplicationStep(3, "Fill Application Form", "Complete either enrollment or update form accurately with your details."),
        ApplicationStep(4, "Biometric & Demographic Capture", "At the center, photo, 10 fingerprints, and dual iris scan are recorded."),
        ApplicationStep(5, "Collect Acknowledgment Slip", "Receive printed enrollment slip with 28-digit Enrollment ID (EID)."),
        ApplicationStep(6, "Track & Download e-Aadhaar", "Track status online; once generated, download password-protected e-Aadhaar PDF.")
      ),
      importantNotes = listOf(
        "Always carry original physical documents to the enrollment center for scanning.",
        "Enrollment for Aadhaar is completely free; nominal fee applies only for demographic/biometric updates.",
        "Ensure your mobile number is linked for receiving OTPs during future online verifications."
      ),
      commonRejections = listOf(
        "Name spelling mismatch between Proof of Identity and Address documents.",
        "Submitting electricity or utility bills older than 3 months.",
        "Blurry photocopies or laminated original documents where seal cannot be verified."
      ),
      faqs = listOf(
        FaqItem("Is Aadhaar enrollment mandatory?", "Aadhaar is voluntary for basic residency but required for government subsidies, tax filing, and major banking services."),
        FaqItem("How long does generation take?", "Generally between 15 to 90 days after biometric verification."),
        FaqItem("What if name spelling differs in documents?", "Minor variations may require gazette notification or standard UIDAI certificate signed by an authorized gazetted officer.")
      ),
      keywords = listOf("aadhaar", "uidai", "aadhar", "adhaar", "biometric", "e-aadhaar", "uid", "identity card")
    ),

    // 2. PAN Card
    ServiceItem(
      id = "pan",
      name = "PAN Card",
      shortDescription = "Permanent Account Number for tax filing, banking, and financial transactions.",
      description = "PAN is a ten-digit alphanumeric identifier issued by the Income Tax Department to track financial transactions and curb tax evasion. Essential for high-value financial activities and bank accounts.",
      category = ServiceCategory.IDENTITY,
      department = "Income Tax Department (Protean / NSDL & UTIITSL)",
      officialPortalUrl = "https://www.onlineservices.nsdl.com",
      estimatedFee = "₹107 for physical card dispatch in India | ₹72 for e-PAN only",
      processingTime = "10 to 15 business days (Instant e-PAN within 10 mins via Aadhaar)",
      applicationMode = "100% Online Paperless (e-KYC) or Physical Courier",
      requiredDocuments = listOf(
        DocumentItem(
          id = "pan_poi",
          name = "Proof of Identity (PoI)",
          purpose = "To authenticate the legal identity and signature of the applicant.",
          examples = listOf("Aadhaar Card", "Voter ID Card", "Driving Licence", "Passport", "Arm's Licence", "Photo ID issued by Central/State Gov"),
          required = true,
          notes = "Aadhaar is the preferred document as it enables instant paperless e-KYC."
        ),
        DocumentItem(
          id = "pan_poa",
          name = "Proof of Address (PoA)",
          purpose = "To deliver the physical PAN card and establish legal residence.",
          examples = listOf("Aadhaar Card", "Electricity Bill (within 3 months)", "Broadband Bill", "Bank Account Statement", "Post Office Passbook"),
          required = true,
          notes = "The physical card will be dispatched by post to this exact registered address."
        ),
        DocumentItem(
          id = "pan_dob",
          name = "Proof of Date of Birth (DoB)",
          purpose = "To record date of birth and verify age requirements.",
          examples = listOf("Birth Certificate", "Matriculation / 10th Mark Sheet", "Passport", "Driving Licence", "Aadhaar Card"),
          required = true,
          notes = "Date format DD/MM/YYYY must strictly match supporting papers."
        ),
        DocumentItem(
          id = "pan_photo",
          name = "Passport Size Photographs",
          purpose = "Two recent color photographs (3.5cm x 2.5cm) printed onto the physical card.",
          examples = listOf("Recent white-background color passport photographs"),
          required = true,
          notes = "Not needed if using instant Aadhaar paperless e-Sign mode."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "pan_rep",
          name = "Representative Assessee Documents",
          purpose = "Only if applicant is a minor, mentally incapacitated, or non-resident.",
          examples = listOf("Parent/Guardian Aadhaar and proof of relationship"),
          required = false,
          notes = "The parent signs the PAN form on behalf of minor child."
        )
      ),
      eligibilityCriteria = listOf(
        "Any individual, minor (represented by guardian), partnership firm, trust, or company.",
        "Indian citizens as well as foreign citizens conducting business or investments in India.",
        "No minimum age limit exists (minors receive minor PAN without photograph, converted upon turning 18)."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Select Portal (NSDL / UTIITSL)", "Choose Form 49A for Indian citizens or Form 49AA for foreign nationals."),
        ApplicationStep(2, "Select Application Mode", "Choose between Paperless e-KYC (Instant Aadhaar OTP) or Physical Document Submission."),
        ApplicationStep(3, "Fill Personal & Parent Details", "Provide father's or mother's name to be printed on the card, and income source."),
        ApplicationStep(4, "Choose AO Code", "Select Assessing Officer (AO) code based on state and city."),
        ApplicationStep(5, "Pay Application Fee", "Pay approximately ₹107 for physical card in India or ₹72 for e-PAN only via debit/credit card or UPI."),
        ApplicationStep(6, "e-Sign & Track Status", "Digitally e-Sign via Aadhaar OTP or post physical signed form to NSDL office. Card arrives in 10-15 days.")
      ),
      importantNotes = listOf(
        "Holding more than one PAN card is illegal and subject to a penalty of ₹10,000 under Section 272B of Income Tax Act.",
        "Linking PAN with Aadhaar is mandatory for Indian tax filers.",
        "Double-check father's name spelling as it is standard identifier on Indian PAN cards."
      ),
      commonRejections = listOf(
        "Father's name spelling differs from educational records.",
        "Signature going outside the white signature box.",
        "Blurry photo upload when applying without Aadhaar OTP."
      ),
      faqs = listOf(
        FaqItem("Can I get an instant PAN online?", "Yes, Instant e-PAN can be generated within minutes using UIDAI Aadhaar e-KYC on the Income Tax e-filing portal free of charge."),
        FaqItem("Can students without income get a PAN?", "Yes, select 'No Income' in the income sources column. Students need PAN for education loans and opening bank accounts.")
      ),
      keywords = listOf("pan", "pan card", "income tax", "nsdl", "utiitsl", "tin", "permanent account number", "tax id")
    ),

    // 3. Passport
    ServiceItem(
      id = "passport",
      name = "Passport",
      shortDescription = "Official Indian travel document for international travel and global identity.",
      description = "Issued by the Ministry of External Affairs through Passport Seva Kendras (PSK). Serves as paramount proof of Indian citizenship, identity, and international travel authorization.",
      category = ServiceCategory.TRAVEL,
      department = "Consular, Passport & Visa Division, Ministry of External Affairs",
      officialPortalUrl = "https://www.passportindia.gov.in",
      estimatedFee = "Normal: ₹1,500 (36 pages) / ₹2,000 (60 pages) | Tatkaal: ₹3,500",
      processingTime = "Normal: 7 to 15 days post police verification | Tatkaal: 1 to 3 days",
      applicationMode = "Online Form + Mandatory PSK In-Person Verification",
      requiredDocuments = listOf(
        DocumentItem(
          id = "pass_poa",
          name = "Proof of Present Address",
          purpose = "To verify current residential address for local police verification.",
          examples = listOf("Aadhaar Card", "Electricity Bill", "Water Bill", "Telephone/Mobile Bill", "Active Bank Passbook with Photo", "Rent Agreement (registered)"),
          required = true,
          notes = "Must show present residence where you have lived for the past 1 year."
        ),
        DocumentItem(
          id = "pass_dob",
          name = "Proof of Date of Birth",
          purpose = "To verify exact birth date and birthplace details.",
          examples = listOf("Birth Certificate issued by Registrar of Births & Deaths", "School Leaving / Secondary Board Certificate", "PAN Card", "Aadhaar Card", "Driving Licence"),
          required = true,
          notes = "For applicants born on or after 26/01/1989, birth certificate was earlier required; now multiple documents accepted under revised MEA rules."
        ),
        DocumentItem(
          id = "pass_ecnr",
          name = "Non-ECR (Emigration Check Not Required) Proof",
          purpose = "Exempts travelers from emigration checks when traveling abroad for work.",
          examples = listOf("Class 10 (SSC/Matriculation) Pass Certificate", "Higher Educational Degree", "Income Tax assessment proof (3 years)"),
          required = true,
          notes = "If 10th pass certificate is not provided, passport will be issued under ECR category."
        ),
        DocumentItem(
          id = "pass_annex",
          name = "Annexure / Declaration",
          purpose = "Standard declaration affirming Indian citizenship and lack of criminal records.",
          examples = listOf("Annexure 'E' standard self-declaration"),
          required = true,
          notes = "Generated automatically or downloadable from the Passport Seva portal."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "pass_noc",
          name = "NOC / Prior Intimation Letter",
          purpose = "Mandatory only for Government / PSU employees.",
          examples = listOf("Annexure M or Annexure N from employer department"),
          required = false,
          notes = "Only required for central or state government servants."
        )
      ),
      eligibilityCriteria = listOf(
        "Must be a citizen of India by birth, descent, registration, or naturalization.",
        "Must not have active criminal court proceedings or warrants against you.",
        "Minors require consent of both parents (Annexure C or D).",
        "Adult passports are issued with 10-year validity; minors get 5-year validity or until age 18."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Register on Passport Seva Portal", "Create an account on www.passportindia.gov.in with valid email address."),
        ApplicationStep(2, "Fill Online Application Form", "Select 'Apply for Fresh Passport / Re-issue' and enter personal, family, and address details."),
        ApplicationStep(3, "Pay and Schedule Appointment", "Online payment (approx ₹1,500 normal or ₹3,500 Tatkaal) is mandatory to book slot at PSK/POPSK."),
        ApplicationStep(4, "Visit Passport Seva Kendra (PSK)", "Bring all original documents along with self-attested photocopies. Biometrics and photo are clicked at PSK."),
        ApplicationStep(5, "Police Verification", "Local police station visits your address or calls you for physical document verification."),
        ApplicationStep(6, "Dispatch by Speed Post", "Once approved, passport is printed and safely delivered by India Post Speed Post.")
      ),
      importantNotes = listOf(
        "Never submit laminated certificates if stamps cannot be inspected clearly.",
        "Ensure address on documents exactly matches the residence where police verification will happen.",
        "Tatkaal scheme requires three specific proofs (like Aadhaar, PAN, Voter ID) and gives urgent delivery."
      ),
      commonRejections = listOf(
        "Address proof not reflecting current physical residence (causes adverse police verification report).",
        "Name mismatch between Matriculation certificate and Aadhaar card.",
        "Failing to disclose past applications or pending criminal court cases."
      ),
      faqs = listOf(
        FaqItem("Do I need to take passport size photos to PSK?", "No, digital photographs and fingerprints are taken directly inside the PSK booth."),
        FaqItem("What is Tatkaal scheme?", "An expedited service where passports are printed within 1-3 working days for emergency travel with higher fee.")
      ),
      keywords = listOf("passport", "psk", "travel", "international travel", "mea", "tatkaal", "visa", "emigration", "ecnr")
    ),

    // 4. Driving Licence
    ServiceItem(
      id = "driving_licence",
      name = "Driving Licence",
      shortDescription = "Official permit to drive motor vehicles on public roads (Learner's & Permanent).",
      description = "Issued by Regional Transport Offices (RTO) across Indian States through the Sarathi Parivahan portal. Authorizes driving specific classes of vehicles (Motorcycle, Light Motor Vehicle, Commercial).",
      category = ServiceCategory.TRANSPORT,
      department = "Ministry of Road Transport and Highways (MoRTH / Sarathi)",
      officialPortalUrl = "https://parivahan.gov.in/parivahan",
      estimatedFee = "₹700 to ₹1,000 depending on vehicle classes & state smart card charges",
      processingTime = "Learner's: Same day | Permanent DL: 7 to 15 days post RTO track test",
      applicationMode = "Online Learner Test + RTO Practical Driving Track Test",
      requiredDocuments = listOf(
        DocumentItem(
          id = "dl_ll",
          name = "Learner's Licence (LL)",
          purpose = "Prerequisite permit holding mandatory driving period before permanent DL test.",
          examples = listOf("Valid Learner's Licence issued at least 30 days prior"),
          required = true,
          notes = "Permanent DL can only be applied after 30 days and within 180 days of LL issuance."
        ),
        DocumentItem(
          id = "dl_age",
          name = "Proof of Age",
          purpose = "To ensure candidate meets statutory age limits (18+ for LMV, 16+ for 50cc gearless).",
          examples = listOf("10th School Passing Certificate", "Birth Certificate", "Aadhaar Card", "Passport", "PAN Card"),
          required = true,
          notes = "Applicant must be minimum 18 years for private cars and two-wheelers with gear."
        ),
        DocumentItem(
          id = "dl_poa",
          name = "Proof of Address",
          purpose = "Determines the jurisdiction of the Regional Transport Office (RTO).",
          examples = listOf("Aadhaar Card", "Voter ID Card", "Passport", "Electricity Bill", "Life Insurance Policy"),
          required = true,
          notes = "Must belong to the district jurisdiction where RTO test is booked."
        ),
        DocumentItem(
          id = "dl_med",
          name = "Medical Certificate (Form 1A)",
          purpose = "To verify vision, physical fitness, and medical ability to drive.",
          examples = listOf("Form 1A signed by a Registered Medical Practitioner (MBBS)"),
          required = true,
          notes = "Mandatory for transport/commercial vehicles and applicants above 40 years."
        ),
        DocumentItem(
          id = "dl_form1",
          name = "Physical Fitness Self-Declaration (Form 1)",
          purpose = "Self-declaration of physical ability for non-transport category.",
          examples = listOf("Form 1 filled online during Sarathi application"),
          required = true,
          notes = "Standard online checklist about eyesight and physical wellness."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "dl_ds",
          name = "Driving School Certificate (Form 5)",
          purpose = "Certifies formal training at an accredited motor driving training school.",
          examples = listOf("Form 5 issued by registered driving training school"),
          required = false,
          notes = "Mandatory for commercial/transport vehicle licences."
        )
      ),
      eligibilityCriteria = listOf(
        "Age 16+: Two-wheeler without gear (engine capacity up to 50cc) with parental consent.",
        "Age 18+: Light Motor Vehicle (LMV) including private cars and standard motorcycles with gear.",
        "Age 20+: Transport / Commercial vehicles (must hold LMV license for at least 1 year in many states).",
        "Must hold valid Learner's Licence for at least 30 days before permanent driving test."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Apply for Learner's Licence (LL)", "Log on to Sarathi Parivahan, fill application, upload documents, and take online LL road safety test."),
        ApplicationStep(2, "Practice Driving for 30 Days", "Drive with 'L' board accompanied by an experienced licensed driver."),
        ApplicationStep(3, "Apply for Permanent DL Slot", "Visit Parivahan, choose 'Apply for Driving Licence', enter LL number and book RTO driving track test slot."),
        ApplicationStep(4, "Appear for Driving Test at RTO", "Demonstrate reverse S, H track, parallel parking, and road traffic adherence in front of Motor Vehicle Inspector (MVI)."),
        ApplicationStep(5, "Biometric & Fee Payment", "Pay licence fee (around ₹700–₹1,000 including smart card) and capture biometrics at RTO counter."),
        ApplicationStep(6, "Receive Smart Card DL", "The smart card driving licence is posted to your home or downloadable via DigiLocker / mParivahan.")
      ),
      importantNotes = listOf(
        "Learner's Licence is valid for 6 months across India.",
        "Never drive alone on a Learner's Licence; an instructor or licensed driver must sit in passenger seat.",
        "Electronic DL in DigiLocker or mParivahan app is legally equivalent to physical card under IT Act."
      ),
      commonRejections = listOf(
        "Failing the automated RTO camera sensor track (touching side yellow lines).",
        "Applying before the mandatory 30-day waiting period from Learner's Licence issue.",
        "Learner's Licence expired (> 180 days) prior to booking practical test."
      ),
      faqs = listOf(
        FaqItem("What if I fail the driving test?", "You can book a re-test after 7 days by paying a nominal re-test fee on the Sarathi portal."),
        FaqItem("Can I drive outside my home state with my DL?", "Yes, an Indian Driving Licence issued by any state RTO is valid throughout the territory of India.")
      ),
      keywords = listOf("driving licence", "driving license", "dl", "rto", "learner licence", "parivahan", "sarathi", "car licence", "bike licence")
    ),

    // 5. Voter ID
    ServiceItem(
      id = "voter_id",
      name = "Voter ID (EPIC)",
      shortDescription = "Elector's Photo Identity Card for voting in parliamentary and assembly elections.",
      description = "Issued by the Election Commission of India (ECI) to registered electors. Serves as official government proof of citizenship and voting entitlement in democratic elections.",
      category = ServiceCategory.IDENTITY,
      department = "Election Commission of India (ECI)",
      officialPortalUrl = "https://voters.eci.gov.in",
      estimatedFee = "100% Free of Cost (Delivered free by Speed Post)",
      processingTime = "15 to 30 days (Includes Booth Level Officer home verification)",
      applicationMode = "100% Online via ECI Portal or Voter Helpline App",
      requiredDocuments = listOf(
        DocumentItem(
          id = "vot_photo",
          name = "Recent Passport Photograph",
          purpose = "To print on the EPIC card and electoral roll register.",
          examples = listOf("Recent color photograph with white background (4.5cm x 3.5cm)"),
          required = true,
          notes = "Face must be clearly visible looking directly forward."
        ),
        DocumentItem(
          id = "vot_age",
          name = "Proof of Age / Date of Birth",
          purpose = "To verify attainment of universal adult suffrage age (18 years).",
          examples = listOf("Birth Certificate", "Aadhaar Card", "PAN Card", "Driving Licence", "CBSE/ICSE Class 10 Certificate", "Indian Passport"),
          required = true,
          notes = "Must have turned 18 on or before the qualifying qualifying dates."
        ),
        DocumentItem(
          id = "vot_addr",
          name = "Proof of Ordinary Residence",
          purpose = "Determines assembly constituency and polling booth polling station.",
          examples = listOf("Aadhaar Card", "Current Bank / Post Office Passbook", "Electricity/Gas/Water Bill", "Registered Sale Deed / Rent Agreement", "Indian Passport"),
          required = true,
          notes = "Must reflect ordinary residence where you currently stay."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "vot_family",
          name = "Family Member EPIC Number",
          purpose = "Ensures enrollment in the exact same polling station/section as parents/spouse.",
          examples = listOf("Father's, Mother's or Spouse's Voter ID card number"),
          required = false,
          notes = "Highly recommended for fast family pooling in voter lists."
        )
      ),
      eligibilityCriteria = listOf(
        "Must be an Indian citizen.",
        "Must have completed 18 years of age on any of the four qualifying dates (Jan 1, Apr 1, Jul 1, Oct 1).",
        "Must be an ordinary resident of the constituency area where applying.",
        "Must not be disqualified under any law due to unsoundness of mind or corrupt practices."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Open Voters' Service Portal", "Visit voters.eci.gov.in or download ECINet / Voter Helpline App."),
        ApplicationStep(2, "Select Form 6", "Form 6 is for New Voter Registration (Form 8 is for shifting/corrections)."),
        ApplicationStep(3, "Fill Personal & Polling Info", "Select your state, district, and assembly constituency. Enter name, age, and address."),
        ApplicationStep(4, "Upload Proofs & Photo", "Upload scanned copy of Age Proof, Address Proof, and photograph in JPG/PDF format."),
        ApplicationStep(5, "Submit & Note Reference ID", "Submit online to get a unique application reference number for status tracking."),
        ApplicationStep(6, "Booth Level Officer (BLO) Verification", "BLO visits for field verification. Once approved, e-EPIC can be downloaded, and physical card is delivered free.")
      ),
      importantNotes = listOf(
        "Enrollment as a voter is completely free of charge. No fee is payable to any authority or agent.",
        "Registering in more than one constituency or polling station is an offense under the Representation of the People Act.",
        "Digital e-EPIC downloaded from ECI portal is legally valid for voting with any other photo ID."
      ),
      commonRejections = listOf(
        "Incomplete address details lacking house/ward number preventing BLO physical verification.",
        "Attempting dual enrollment in hometown while studying/working in a new city.",
        "Unclear or blurry selfie photo instead of white-background passport photo."
      ),
      faqs = listOf(
        FaqItem("How do I download e-EPIC?", "Once enrolled and mobile linked, log in to voters.eci.gov.in and click 'e-EPIC Download'."),
        FaqItem("Can college students register from hostel address?", "Yes, students can register either at their native place or at their educational institution hostel with hostel certificate.")
      ),
      keywords = listOf("voter id", "voter card", "epic", "election", "election commission", "form 6", "blo", "voting card")
    ),

    // 6. Income Certificate
    ServiceItem(
      id = "income_certificate",
      name = "Income Certificate",
      shortDescription = "Official certificate stating annual family income for scholarships, subsidies & fee waivers.",
      description = "Issued by revenue authorities (Tehsildar / Sub-Divisional Magistrate / Revenue Officer) under state governments. Verifies the total annual income of an individual or family from all sources.",
      category = ServiceCategory.CERTIFICATES,
      department = "State Revenue & District Administration Department",
      officialPortalUrl = "https://services.india.gov.in",
      estimatedFee = "₹15 to ₹50 statutory government processing fee",
      processingTime = "7 to 15 working days",
      applicationMode = "State e-District Portal or Citizen Service Center (CSC)",
      statePortals = standardStatePortals,
      requiredDocuments = listOf(
        DocumentItem(
          id = "inc_id",
          name = "Proof of Identity",
          purpose = "To establish the applicant or family head identity.",
          examples = listOf("Aadhaar Card", "Voter ID Card", "PAN Card", "Ration Card"),
          required = true,
          notes = "Preferably Aadhaar of the family earner."
        ),
        DocumentItem(
          id = "inc_addr",
          name = "Proof of Residence",
          purpose = "To confirm jurisdiction of local Taluk / Tehsil office.",
          examples = listOf("Ration Card", "Electricity Bill", "Domicile Certificate", "Voter ID", "Water Bill"),
          required = true,
          notes = "Address must fall within the Revenue Village / Ward."
        ),
        DocumentItem(
          id = "inc_proof",
          name = "Income Proof / Salary Slip / Form 16",
          purpose = "Evidence of monthly/annual earnings from employment or profession.",
          examples = listOf("Salary Certificate / Pay Slip (last 3 months)", "Form 16 / ITR assessment", "Agricultural Income Land Record (7/12 extract or Patta)", "Pension Payment Order"),
          required = true,
          notes = "Self-employed or daily wage earners provide an affidavit."
        ),
        DocumentItem(
          id = "inc_affidavit",
          name = "Self-Declaration Affidavit",
          purpose = "Notarized or self-declaration affirming total family income from all sources.",
          examples = listOf("Prescribed state affidavit format on stamp paper or e-declaration"),
          required = true,
          notes = "False income declarations carry legal prosecution for fraud."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "inc_ration",
          name = "BPL Ration Card Copy",
          purpose = "For families falling under Below Poverty Line quota.",
          examples = listOf("Yellow/Antyodaya ration card"),
          required = false,
          notes = "Fast-tracks verification in most state portals."
        )
      ),
      eligibilityCriteria = listOf(
        "Any resident citizen of the state/UT requiring income verification for education, welfare, or legal quotas.",
        "Total household income calculation includes earnings of applicant, spouse, and dependent children.",
        "Agricultural and non-agricultural incomes must be segregated as per state revenue guidelines."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Access State e-District Portal", "Open your state's citizen service portal (e.g., Aaple Sarkar in MH, Seva Sindhu in KA, e-District UP/Delhi)."),
        ApplicationStep(2, "Select Revenue Department", "Choose 'Issue of Income Certificate' service under revenue division."),
        ApplicationStep(3, "Fill Income & Family Details", "Detail family members, occupations, agricultural land if any, and business income."),
        ApplicationStep(4, "Upload Signed Affidavit & Proofs", "Attach salary slips/ITR/land patta along with identity and address proofs."),
        ApplicationStep(5, "Pay Nominal Fee", "Pay government processing fee (usually ₹15–₹50 depending on state)."),
        ApplicationStep(6, "Verification by Talathi / Patwari", "Village Revenue Assistant (Talathi/Patwari) verifies records and submits report to Tehsildar for digital sign.")
      ),
      importantNotes = listOf(
        "Most income certificates are valid for 1 financial year (from April 1 to March 31).",
        "Calculate gross annual family income, not just take-home salary, as government quotas review gross totals.",
        "Digital certificates with QR code require no manual seal or physical signature under Digital India."
      ),
      commonRejections = listOf(
        "Under-reporting family business or agricultural income compared to land records.",
        "Declaring individual student income instead of total household parental income.",
        "Expired self-declaration affidavit format or missing notary seal."
      ),
      faqs = listOf(
        FaqItem("How many days does it take to issue?", "Standard processing time across most states is 7 to 15 working days."),
        FaqItem("Is Income Certificate accepted across states?", "Generally yes for Central Government schemes (UPSC, Central Universities), but some state quotas prefer domicile state certificates.")
      ),
      keywords = listOf("income certificate", "income proof", "tehsildar", "affidavit", "tahsildar", "e-district", "scholarship income", "revenue certificate")
    ),

    // 7. Caste Certificate
    ServiceItem(
      id = "caste_certificate",
      name = "Caste Certificate",
      shortDescription = "Official proof of belonging to Scheduled Caste (SC), Scheduled Tribe (ST), or OBC.",
      description = "Issued by District Magistrate / Sub-Divisional Officer (SDO) / Tehsildar. Crucial for availing reservation benefits in educational admissions, government jobs, scholarships, and statutory quotas.",
      category = ServiceCategory.CERTIFICATES,
      department = "Social Welfare & Revenue Department",
      officialPortalUrl = "https://services.india.gov.in",
      estimatedFee = "₹20 to ₹60 statutory government fee",
      processingTime = "15 to 45 working days (Requires lineage scrutiny)",
      applicationMode = "State e-District Portal / Sub-Divisional Magistrate Office",
      statePortals = standardStatePortals,
      requiredDocuments = listOf(
        DocumentItem(
          id = "cst_id",
          name = "Proof of Identity",
          purpose = "To verify applicant's personal details.",
          examples = listOf("Aadhaar Card", "Voter ID Card", "PAN Card", "Student ID Card"),
          required = true,
          notes = "Aadhaar of applicant or father."
        ),
        DocumentItem(
          id = "cst_addr",
          name = "Proof of Address",
          purpose = "To confirm family residence in the district of origin.",
          examples = listOf("Ration Card", "Electricity Bill", "Domicile Certificate", "Water Bill"),
          required = true,
          notes = "Must establish ancestral or historical residence in the state."
        ),
        DocumentItem(
          id = "cst_ancestry",
          name = "Paternal Caste Proof (Father / Blood Relative)",
          purpose = "Vital proof demonstrating that father or grandfather belongs to the claimed community.",
          examples = listOf("Father's School Leaving Certificate with Caste entry", "Grandfather's Land Revenue Record / Kotwal Book entry", "Father's Caste Certificate", "Uncle/Blood Relative Caste Certificate with family genealogy tree (Vanshavali)"),
          required = true,
          notes = "Caste is strictly derived through paternal lineage in Indian law."
        ),
        DocumentItem(
          id = "cst_decl",
          name = "Affidavit / Self Declaration",
          purpose = "Legal declaration of community, religion, and family ancestry.",
          examples = listOf("State prescribed Caste Affidavit on judicial stamp paper"),
          required = true,
          notes = "Must explicitly declare sub-caste and category."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "cst_ncl",
          name = "Non-Creamy Layer (NCL) Proof",
          purpose = "Mandatory for OBC candidates claiming job/education reservation.",
          examples = listOf("Last 3 years Income Tax Returns / Income Certificate of parents"),
          required = false,
          notes = "Required only for OBC category to verify income is under current ceiling (₹8 Lakhs/annum)."
        )
      ),
      eligibilityCriteria = listOf(
        "Applicant must belong to a recognized Scheduled Caste, Scheduled Tribe, or Other Backward Class specified in the President's order for that state.",
        "Candidate's family must have been permanent residents of the state prior to the cutoff year (e.g., 1950 for SC/ST, 1967 in Maharashtra, etc.).",
        "Caste of the father is the determining legal benchmark."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Collect Paternal Records", "Retrieve father's or grandfather's old school records, land documents, or village Kotwal book extract with caste mentioned."),
        ApplicationStep(2, "Draft Family Tree (Vanshavali)", "Prepare genealogical chart showing direct relationship with the relative holding proof."),
        ApplicationStep(3, "Submit Application Online / SDO Office", "Apply via State e-District portal or at Citizen Service Center (CSC / Maha e-Seva)."),
        ApplicationStep(4, "Field Inquiry by Revenue Inspector", "Local inquiry is conducted to verify family heritage and native place."),
        ApplicationStep(5, "Scrutiny & Approval", "SDO / Tehsildar approves application upon verification of historical documentation."),
        ApplicationStep(6, "Receive Caste Certificate", "Download digitally signed barcode/QR certificate or collect certified copy.")
      ),
      importantNotes = listOf(
        "SC and ST certificates generally have lifetime validity, whereas OBC Non-Creamy Layer (NCL) certificates must be renewed periodically (usually annually).",
        "Mother's caste proof is generally not accepted for claiming reservation except in specific rare court-approved single mother cases.",
        "Keep digital certificate safely stored in DigiLocker for college admissions."
      ),
      commonRejections = listOf(
        "Submitting mother's caste records instead of paternal lineage records.",
        "Lack of historical evidence showing residence in state prior to the statutory cutoff year.",
        "Discrepancies in sub-caste naming between school register and revenue records."
      ),
      faqs = listOf(
        FaqItem("What is the difference between Caste Certificate and Caste Validity?", "Caste Certificate certifies your caste, while Caste Validity Certificate is issued after scrutiny by a government Scrutiny Committee before engineering/medical admissions or elections."),
        FaqItem("Can I get a caste certificate from another state where I migrated?", "You can only get a migrated caste certificate with limited benefits; full state reservation requires certificate from native state where family resided before cutoff year.")
      ),
      keywords = listOf("caste certificate", "caste", "sc", "st", "obc", "non creamy layer", "ncl", "social welfare", "reservation")
    ),

    // 8. Domicile Certificate
    ServiceItem(
      id = "domicile_certificate",
      name = "Domicile Certificate",
      shortDescription = "Proof of permanent residence in a particular state for education and job quotas.",
      description = "Issued by revenue authorities (Tehsildar / Magistrate). Certifies that a person has been a continuous permanent resident in the state for a specified number of years (often 10–15 years) or by birth.",
      category = ServiceCategory.CERTIFICATES,
      department = "State Revenue & Home Affairs Department",
      officialPortalUrl = "https://services.india.gov.in",
      estimatedFee = "₹15 to ₹50 statutory fee",
      processingTime = "10 to 21 working days",
      applicationMode = "State e-District Citizen Portal",
      statePortals = standardStatePortals,
      requiredDocuments = listOf(
        DocumentItem(
          id = "dom_id",
          name = "Proof of Identity",
          purpose = "Personal identification of the applicant.",
          examples = listOf("Aadhaar Card", "Voter ID", "PAN Card", "Passport"),
          required = true,
          notes = "Aadhaar with matching current address."
        ),
        DocumentItem(
          id = "dom_res_proof",
          name = "Proof of Continuous Residence",
          purpose = "Evidence proving continuous stay for minimum qualifying period (10-15 years).",
          examples = listOf("School Leaving Certificates showing multi-year education in state", "Electricity Bills covering multiple years", "Property Tax Receipts / House Registry", "Parents' residential proofs"),
          required = true,
          notes = "Must satisfy state-mandated continuous residence duration."
        ),
        DocumentItem(
          id = "dom_birth",
          name = "Proof of Birth in the State",
          purpose = "To verify birthplace within state boundary.",
          examples = listOf("Birth Certificate issued by Local Body", "10th Class Certificate with birthplace mentioned"),
          required = true,
          notes = "Birth inside state significantly eases eligibility."
        ),
        DocumentItem(
          id = "dom_aff",
          name = "Affidavit of Domicile",
          purpose = "Sworn statement affirming intention of permanent stay and that no other state domicile is held.",
          examples = listOf("Notarized affidavit on standard judicial stamp paper"),
          required = true,
          notes = "Holding domicile of two states simultaneously is illegal."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "dom_emp",
          name = "Parent's Government Employment Order",
          purpose = "For children of state/central government officers stationed in the state.",
          examples = listOf("Service certificate and transfer/posting order"),
          required = false,
          notes = "Relaxes strict multi-year continuous residence requirement."
        )
      ),
      eligibilityCriteria = listOf(
        "Candidate's parents must be permanent residents of the state, or",
        "Candidate has resided continuously in the state for the duration prescribed (usually 10 to 15 years), or",
        "Candidate completed minimum years of schooling (usually 3 to 7 years) in recognized institutions within the state."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Gather Educational & Residence Proofs", "Collect marksheets, school leaving certificate (showing years studied), and municipal property tax/utility records."),
        ApplicationStep(2, "Prepare Affidavit", "Get sworn affidavit affirming permanent residence and declaring no other state domicile exists."),
        ApplicationStep(3, "Submit Application on State Portal", "Login to state citizen portal, upload documents, and pay nominal application fee."),
        ApplicationStep(4, "Field Verification", "Talathi/Patwari inspects residential claims and forwards approval report to Tehsildar."),
        ApplicationStep(5, "Digital Issuance", "Tehsildar / SDO issues digital Domicile Certificate with QR verification code.")
      ),
      importantNotes = listOf(
        "A person can legally hold domicile of only ONE state in India at any given time.",
        "Domicile Certificate has lifetime validity unless revoked or applicant establishes permanent domicile elsewhere.",
        "Essential for state medical/engineering college quota (85% state quota seats)."
      ),
      commonRejections = listOf(
        "Gaps in continuous residence documentation failing the 10-15 year requirement.",
        "Holding active domicile/resident benefits in a neighbouring state.",
        "School leaving certificate failing to specify place of birth and duration of study."
      ),
      faqs = listOf(
        FaqItem("Is Domicile Certificate the same as Residence Proof?", "No. A residence proof (like utility bill) proves where you live today. A Domicile Certificate is a legal confirmation of your permanent home state for statutory rights."),
        FaqItem("Can I get domicile if I was born in another state?", "Yes, if you meet the continuous residence criteria (usually 10-15 years living/studying in the state) specified by that state's government.")
      ),
      keywords = listOf("domicile", "domicile certificate", "residence certificate", "bonafide resident", "native certificate", "state quota")
    ),

    // 9. Birth Certificate
    ServiceItem(
      id = "birth_certificate",
      name = "Birth Certificate",
      shortDescription = "First legal identity document recording the birth of a child.",
      description = "Issued under the Registration of Births and Deaths Act by Municipal Corporations, Municipalities, or Village Panchayats. Paramount proof of age, citizenship, and parentage.",
      category = ServiceCategory.CERTIFICATES,
      department = "Civil Registration System (CRS) / Municipal Administration",
      officialPortalUrl = "https://crsorgi.gov.in",
      estimatedFee = "Free if within 21 days; ₹5-₹20 late fee up to 30 days; Magistrate order fee after 1 year",
      processingTime = "5 to 10 working days",
      applicationMode = "Hospital Direct Reporting + Municipal Portal Download",
      requiredDocuments = listOf(
        DocumentItem(
          id = "brt_slip",
          name = "Hospital Discharge Summary / Birth Slip",
          purpose = "Official hospital record of delivery including time, date, sex, and mother's name.",
          examples = listOf("Institutional birth discharge card / Form 1 from hospital maternity ward"),
          required = true,
          notes = "Issued directly by the hospital/nursing home medical officer."
        ),
        DocumentItem(
          id = "brt_parents_id",
          name = "Identity Proofs of Both Parents",
          purpose = "To confirm exact legal names of father and mother for registration.",
          examples = listOf("Aadhaar Card of Mother and Father", "Voter ID Card", "Passport"),
          required = true,
          notes = "Names on birth record will reflect these documents."
        ),
        DocumentItem(
          id = "brt_marriage",
          name = "Parents' Marriage Certificate / Joint Declaration",
          purpose = "Proof of lawful matrimony for child records.",
          examples = listOf("Marriage Certificate or Joint Affidavit"),
          required = false,
          notes = "Not strictly mandatory if both parents' IDs have matching spouse names."
        ),
        DocumentItem(
          id = "brt_addr",
          name = "Parents' Address Proof",
          purpose = "Establishes registered address at the time of delivery.",
          examples = listOf("Aadhaar Card", "Electricity Bill", "Ration Card"),
          required = true,
          notes = "Determines the jurisdiction of local civil registrar."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "brt_delay_order",
          name = "Magistrate Delayed Registration Order",
          purpose = "Required ONLY if birth was not registered within 1 year of occurrence.",
          examples = listOf("Executive Magistrate / SDO Delayed Registration Order (Form 10)"),
          required = false,
          notes = "Mandatory for late registrations beyond 365 days."
        )
      ),
      eligibilityCriteria = listOf(
        "Any child born within Indian territory or born to Indian citizens aboard.",
        "Normal registration must be completed within 21 days of birth at no charge.",
        "Registrations between 21 to 30 days attract late fee; 30 to 365 days require written permission of District Registrar; beyond 1 year requires Magistrate order."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Hospital Reporting (Form 1)", "For institutional births, hospital directly intimates the local Registrar of Births & Deaths within 21 days."),
        ApplicationStep(2, "Submit Parents' Details", "Provide Aadhaar copies and parents' address to hospital registration desk or local municipal ward office."),
        ApplicationStep(3, "Name Inclusion", "If child's name was not finalized at birth, parents can submit child name addition application within 1 year free of charge."),
        ApplicationStep(4, "Verification by Registrar", "Registrar enters data into Civil Registration System (CRS) portal."),
        ApplicationStep(5, "Download Digital Birth Certificate", "Download certified digital certificate with QR code from crsorgi.gov.in or municipal portal.")
      ),
      importantNotes = listOf(
        "Under the revised Registration of Births and Deaths (Amendment) Act, 2023, the digital birth certificate is the single document for school admission, driving licence, passport, and voter registration.",
        "Always register within 21 days to avoid complex delayed court procedures.",
        "Check spellings of father's and mother's names meticulously before final certificate generation."
      ),
      commonRejections = listOf(
        "Late reporting beyond 21 days without appropriate delayed registration order.",
        "Discrepancies in parent names between hospital discharge memo and official Aadhaar.",
        "Applying in a municipality where birth did not physically take place."
      ),
      faqs = listOf(
        FaqItem("Can I add child name later?", "Yes, parents can add child's name within 12 months without penalty, and up to 15 years upon payment of prescribed late fee."),
        FaqItem("How to get duplicate birth certificate?", "Apply online on the respective Municipal Corporation or state CRS portal with date of birth and parents' names.")
      ),
      keywords = listOf("birth certificate", "birth", "crs", "municipal corporation", "newborn", "delayed birth registration", "vital statistics")
    ),

    // 10. Death Certificate
    ServiceItem(
      id = "death_certificate",
      name = "Death Certificate",
      shortDescription = "Official document stating the date, fact, and cause of death of a deceased person.",
      description = "Issued by the Civil Registrar of Births and Deaths under the Registration of Births & Deaths Act. Essential for inheritance, bank account closures, life insurance claims, and property transfer.",
      category = ServiceCategory.CERTIFICATES,
      department = "Civil Registration System (CRS) / Municipal Health Department",
      officialPortalUrl = "https://crsorgi.gov.in",
      estimatedFee = "Free if registered within 21 days; ₹10-₹50 for certified physical copies",
      processingTime = "3 to 7 working days",
      applicationMode = "Hospital / Crematorium Memo + Municipal Registrar Verification",
      requiredDocuments = listOf(
        DocumentItem(
          id = "dth_cause",
          name = "Medical Certificate of Cause of Death (Form 4/4A)",
          purpose = "Attestation by treating doctor regarding the cause and time of death.",
          examples = listOf("Hospital Form 4 (institutional) or Form 4A signed by registered medical doctor (domiciliary)"),
          required = true,
          notes = "Essential for all hospital or doctor-attended deaths."
        ),
        DocumentItem(
          id = "dth_crem",
          name = "Cremation / Burial Ground Receipt",
          purpose = "Proof that last rites were performed at an authorized municipal facility.",
          examples = listOf("Official slip/receipt from crematorium or cemetery superintendent"),
          required = true,
          notes = "Must clearly state date and time of cremation/burial."
        ),
        DocumentItem(
          id = "dth_id_dec",
          name = "Identity Proof of the Deceased",
          purpose = "To confirm correct name, age, gender, and Aadhaar number.",
          examples = listOf("Aadhaar Card", "PAN Card", "Voter ID Card of deceased person"),
          required = true,
          notes = "Aadhaar number of deceased is verified."
        ),
        DocumentItem(
          id = "dth_id_app",
          name = "Identity Proof of Applicant (Next of Kin)",
          purpose = "To record details of the family member applying for certificate.",
          examples = listOf("Aadhaar Card", "Voter ID of spouse/son/daughter"),
          required = true,
          notes = "Establishes relationship with the deceased."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "dth_postmortem",
          name = "Post-Mortem & Police FIR / Panchnama Report",
          purpose = "Required strictly in cases of unnatural, accidental, or suicidal deaths.",
          examples = listOf("Police Inquest Panchnama and Post-Mortem examination report"),
          required = false,
          notes = "Mandatory only for non-natural deaths."
        )
      ),
      eligibilityCriteria = listOf(
        "Must be reported within 21 days of occurrence to the Registrar of Births & Deaths in the jurisdiction where death occurred.",
        "Eligible applicants: Closest surviving family member (spouse, child, parent) or institutional head."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Obtain Medical Certificate of Cause of Death", "Get signed Form 4/4A from hospital or attending medical practitioner."),
        ApplicationStep(2, "Collect Cremation/Burial Receipt", "Preserve official slip given by crematorium or cemetery staff."),
        ApplicationStep(3, "Submit Registration Form (Form 2)", "Submit within 21 days to local Municipal Ward Office or Gram Panchayat."),
        ApplicationStep(4, "Verification by Registrar", "Registrar verifies cremation register entry and hospital intimation."),
        ApplicationStep(5, "Collection of Death Certificate", "Download digitally signed certificate from CRS/state portal or collect physical stamped copies.")
      ),
      importantNotes = listOf(
        "Deaths occurring at home must be informed to local health officer or ward doctor to obtain cause-of-death memo.",
        "Obtain multiple certified copies as original is needed by banks, LIC, EPFO, and property registrars.",
        "Late registration beyond 21 days requires permission from designated revenue officer or magistrate."
      ),
      commonRejections = listOf(
        "Missing doctor's Cause of Death memo (Form 4/4A).",
        "Missing crematorium or burial ground counter receipt.",
        "Unnatural death reported without police Panchnama report."
      ),
      faqs = listOf(
        FaqItem("Why is Aadhaar of deceased required?", "To facilitate deactivation of Aadhaar and prevent misuse or identity theft post demise."),
        FaqItem("What if death occurred during transit/travel?", "Registration is done at the place where the body was offloaded or first removed from transport.")
      ),
      keywords = listOf("death certificate", "death", "cremation slip", "insurance claim", "crs death", "post mortem", "inheritance")
    ),

    // 11. Ration Card
    ServiceItem(
      id = "ration_card",
      name = "Ration Card",
      shortDescription = "Subsidized food grain entitlement and household identity document (NFSA).",
      description = "Issued by the Department of Food, Civil Supplies and Consumer Affairs. Categorized into Antyodaya (AAY), Priority Household (PHH/BPL), and Non-Priority (APL) under National Food Security Act.",
      category = ServiceCategory.BENEFITS,
      department = "Food, Civil Supplies & Consumer Protection Department",
      officialPortalUrl = "https://nfsa.gov.in",
      estimatedFee = "₹20 to ₹50 for smart card printing / digital ONORC issuance",
      processingTime = "15 to 30 working days",
      applicationMode = "State Civil Supplies Portal or Ration Inspector Verification",
      statePortals = standardStatePortals,
      requiredDocuments = listOf(
        DocumentItem(
          id = "rat_head_id",
          name = "Aadhaar Card of Head of Family (Eldest Female)",
          purpose = "Under NFSA 2013, the eldest woman (18+) is designated Head of Family.",
          examples = listOf("Aadhaar Card of mother/wife/eldest adult female member"),
          required = true,
          notes = "If no adult female exists, eldest male becomes head until female member turns 18."
        ),
        DocumentItem(
          id = "rat_members_id",
          name = "Aadhaar Cards of All Family Members",
          purpose = "Mandatory for seeding to allocate per-person monthly grain quota (5kg/person).",
          examples = listOf("Aadhaar cards of all dependents, spouse, and children"),
          required = true,
          notes = "Every member's biometrics and Aadhaar are seeded."
        ),
        DocumentItem(
          id = "rat_addr",
          name = "Proof of Residence / Address",
          purpose = "Allocates beneficiary to nearest Fair Price Shop (FPS) dealer.",
          examples = listOf("Electricity Bill", "Rent Agreement with Landlord NOC", "LPG Gas Connection Passbook", "House Tax Receipt"),
          required = true,
          notes = "Determines Fair Price Ration Shop allocation."
        ),
        DocumentItem(
          id = "rat_income",
          name = "Income Certificate / Income Affidavit",
          purpose = "Determines category entitlement (AAY / BPL / APL).",
          examples = listOf("Revenue authority Income Certificate or BPL Survey list number"),
          required = true,
          notes = "Directly decides whether card is subsidized or non-subsidized."
        ),
        DocumentItem(
          id = "rat_photo",
          name = "Joint Family Photograph",
          purpose = "Printed onto physical ration card booklet or card.",
          examples = listOf("Passport size joint color photo of all living household members"),
          required = true,
          notes = "All listed members should appear clearly."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "rat_surr",
          name = "Surrender Certificate / Deletion Certificate",
          purpose = "Mandatory if moving from another city or married woman moving to in-laws.",
          examples = listOf("Name deletion slip from previous family ration card"),
          required = false,
          notes = "A person's name can never exist on two ration cards concurrently."
        )
      ),
      eligibilityCriteria = listOf(
        "Applicant must be an Indian citizen and resident of the state.",
        "No member of the household should be a member of another existing active ration card.",
        "Family must meet state-defined criteria for poverty line/subsidy brackets."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Delete Name from Old Card (if applicable)", "Get name deletion slip from previous district/state ration card if newly married or moved."),
        ApplicationStep(2, "Open State Food & Civil Supplies Portal", "Access online portal or visit nearest Citizen Service Center (CSC / Ration Kendra)."),
        ApplicationStep(3, "Fill Member & LPG Details", "Enter all family members, their Aadhaar numbers, and LPG consumer number."),
        ApplicationStep(4, "Upload Proofs & Photos", "Upload joint photograph, address proof, income proof, and bank account passbook of female head."),
        ApplicationStep(5, "Inspection by Food Inspector", "Food Supply Officer conducts ground verification of residence and socioeconomic status."),
        ApplicationStep(6, "Issuance & Ration Card Number", "Ration card is approved; collect physical card or use digital One Nation One Ration Card (ONORC).")
      ),
      importantNotes = listOf(
        "Under 'One Nation One Ration Card' (ONORC), beneficiaries can lift their food quota from ANY Fair Price Shop across India using biometric authentication.",
        "Linking Aadhaar of all members is mandatory to prevent bogus or ghost units.",
        "Possessing two active ration cards is a punishable offense under Essential Commodities Act."
      ),
      commonRejections = listOf(
        "Any family member's Aadhaar already active in another ration card unit.",
        "Household possessing four-wheeler or air conditioner in states where this disqualifies BPL status.",
        "Missing name deletion certificate from parents' card after marriage."
      ),
      faqs = listOf(
        FaqItem("Can I use ration card anywhere in India?", "Yes! Under ONORC, biometric e-PoS machines at any ration shop in India can distribute your monthly grain quota."),
        FaqItem("Who is the head of family in ration card?", "Under NFSA guidelines, the eldest woman aged 18 or above in the household is legally designated as Head of Family.")
      ),
      keywords = listOf("ration card", "ration", "food security", "nfsa", "bpl", "apl", "fair price shop", "onorc", "grain quota")
    ),

    // 12. Scholarship Application
    ServiceItem(
      id = "scholarship",
      name = "Scholarship Application",
      shortDescription = "Central and state government educational scholarships (NSP, Post-Matric, Merit).",
      description = "Administered via National Scholarship Portal (NSP) and state portals (e.g., MahaDBT, SSP Karnataka, e-Kalyan). Provides financial assistance and fee reimbursement for pre-matric, post-matric, minority, and merit students.",
      category = ServiceCategory.EDUCATION,
      department = "Ministry of Education / Social Justice & Empowerment",
      officialPortalUrl = "https://scholarships.gov.in",
      estimatedFee = "100% Free of Cost",
      processingTime = "45 to 90 days (Disbursed via PFMS Direct Benefit Transfer)",
      applicationMode = "100% Online via National Scholarship Portal (NSP) or State DBT",
      requiredDocuments = listOf(
        DocumentItem(
          id = "sch_aadhaar",
          name = "Student's Aadhaar Card",
          purpose = "Direct Benefit Transfer (DBT) verification and student identity.",
          examples = listOf("Aadhaar Card linked with active bank account (Aadhaar Seeding)"),
          required = true,
          notes = "Bank account must be Aadhaar-seeded via NPCI mapper to receive scholarship funds."
        ),
        DocumentItem(
          id = "sch_income",
          name = "Parental Annual Income Certificate",
          purpose = "To verify student meets economic ceiling (usually ₹1.5L to ₹2.5L/annum).",
          examples = listOf("Current year Competent Authority Income Certificate issued by Tehsildar"),
          required = true,
          notes = "Must be valid for the current academic session."
        ),
        DocumentItem(
          id = "sch_caste",
          name = "Caste / Category / Minority Certificate",
          purpose = "Required if applying under SC, ST, OBC, EWS, or Minority schemes.",
          examples = listOf("Caste Certificate or Minority Self-Declaration Form"),
          required = false,
          notes = "Not required for General/Merit-only schemes."
        ),
        DocumentItem(
          id = "sch_academic",
          name = "Previous Year Academic Marksheet",
          purpose = "To verify academic eligibility and minimum required percentage (usually 50%-60%+).",
          examples = listOf("10th / 12th / Semester Marksheet signed by Institute"),
          required = true,
          notes = "Marksheet must be attested or original scan."
        ),
        DocumentItem(
          id = "sch_admission",
          name = "Current Year Fee Receipt & College ID",
          purpose = "Proof of active regular enrollment and exact tuition fee paid for reimbursement.",
          examples = listOf("Official college fee receipt with break-up and Bonafide Certificate"),
          required = true,
          notes = "Tuition fee reimbursement is calculated based on this receipt."
        ),
        DocumentItem(
          id = "sch_bank",
          name = "Student's Bank Account Passbook",
          purpose = "Account where scholarship grant is disbursed via PFMS / DBT.",
          examples = listOf("Bank passbook first page showing student's name, IFSC, and account number"),
          required = true,
          notes = "Account must be in student's own name (joint accounts generally not allowed for adults)."
        )
      ),
      optionalDocuments = listOf(
        DocumentItem(
          id = "sch_gap",
          name = "Gap Certificate (if applicable)",
          purpose = "Explains any break in education between previous class and current course.",
          examples = listOf("Notarized Gap Affidavit on non-judicial stamp paper"),
          required = false,
          notes = "Required only if there is a gap year in study."
        ),
        DocumentItem(
          id = "sch_hostel",
          name = "Hostel Warden Certificate",
          purpose = "To claim additional Hosteller maintenance allowance.",
          examples = listOf("Certificate signed by college hostel superintendent"),
          required = false,
          notes = "Required only to claim hosteller rate instead of day-scholar rate."
        )
      ),
      eligibilityCriteria = listOf(
        "Regular student enrolled in a recognized school, college, or university.",
        "Parental annual income must fall below scheme limit (commonly ₹2.5 Lakhs for SC/ST post-matric; ₹1.5L-₹2L for state schemes).",
        "Must have secured minimum qualifying marks (typically 50% or above in last qualifying exam).",
        "Student cannot hold two simultaneous government scholarships for the same course."
      ),
      applicationSteps = listOf(
        ApplicationStep(1, "Aadhaar Bank Seeding", "Visit your bank branch and ensure your bank account is mapped on NPCI server with Aadhaar."),
        ApplicationStep(2, "Register on National/State Portal", "Sign up on scholarships.gov.in (NSP) or respective state DBT portal using Aadhaar."),
        ApplicationStep(3, "Fill Application & Scheme Selection", "Select matching scheme (Post-Matric, Merit-cum-Means, Central Sector, etc.)."),
        ApplicationStep(4, "Upload Documents", "Attach Income Certificate, Caste Proof, Marksheets, Fee Receipt, and Bonafide Certificate."),
        ApplicationStep(5, "Institute Level Verification", "College Nodal Officer (INO) verifies student credentials from college register online."),
        ApplicationStep(6, "District / State Approval & DBT", "State Welfare Officer verifies and sanctions grant; money is directly credited via PFMS to bank account.")
      ),
      importantNotes = listOf(
        "Crucial: Ensure bank account is active and NOT in 'dormant' or 'minor account with limit' status, otherwise DBT payments will bounce.",
        "Never apply for two different government scholarship schemes simultaneously for the same academic year.",
        "Renewals must be submitted each academic year with updated passed marksheets."
      ),
      commonRejections = listOf(
        "Bank account not seeded with Aadhaar in NPCI mapper (PFMS DBT credit bounces).",
        "Applying for multiple government scholarships concurrently for the same year.",
        "Securing lower marks than scheme minimum cutoff in previous academic exam."
      ),
      faqs = listOf(
        FaqItem("What is Aadhaar Seeding in bank?", "Aadhaar seeding links your bank account to the NPCI mapper, enabling government DBT systems to send scholarship funds directly using only your Aadhaar number."),
        FaqItem("Can I edit application after final submission?", "No, you must ask your college scholarship officer to 'Defect' the application so you can correct and re-submit it.")
      ),
      keywords = listOf("scholarship", "nsp", "national scholarship portal", "dbt", "post matric", "fee reimbursement", "bonafide", "education grant")
    )
  )

  fun getServiceById(id: String): ServiceItem? {
    return services.find { it.id.equals(id, ignoreCase = true) }
  }

  fun searchServices(query: String, category: ServiceCategory = ServiceCategory.ALL): List<ServiceItem> {
    val cleanQuery = query.trim().lowercase()
    val matches = services.filter { service ->
      val matchesCategory = (category == ServiceCategory.ALL || service.category == category)
      if (cleanQuery.isEmpty()) {
        matchesCategory
      } else {
        matchesCategory && (
          service.name.lowercase().contains(cleanQuery) ||
            service.shortDescription.lowercase().contains(cleanQuery) ||
            service.description.lowercase().contains(cleanQuery) ||
            service.category.displayName.lowercase().contains(cleanQuery) ||
            service.keywords.any { it.lowercase().contains(cleanQuery) } ||
            service.requiredDocuments.any { it.name.lowercase().contains(cleanQuery) }
        )
      }
    }

    if (cleanQuery.isEmpty()) return matches

    return matches.sortedByDescending { service ->
      val nameLower = service.name.lowercase()
      when {
        nameLower == cleanQuery -> 100
        nameLower.startsWith(cleanQuery) -> 80
        nameLower.contains(cleanQuery) -> 60
        service.keywords.any { it.lowercase() == cleanQuery } -> 40
        service.keywords.any { it.lowercase().startsWith(cleanQuery) } -> 30
        service.keywords.any { it.lowercase().contains(cleanQuery) } -> 20
        else -> 0
      }
    }
  }

  fun getServicesByCategory(category: ServiceCategory): List<ServiceItem> {
    if (category == ServiceCategory.ALL) return services
    return services.filter { it.category == category }
  }
}
