package ch.puzzle.pctsmigration.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

class MatchingServiceTest {
    private MatchingService matchingService;

    @BeforeEach
    void setUp() {
        matchingService = new MatchingService();
    }

    @ParameterizedTest
    @MethodSource("generateData")
    void shouldFindMatchingEntities(List<String> options, String target, String expected) {
        String result = matchingService.match(options, target);

        Assertions.assertEquals(expected, result);

    }

    private static Stream<Arguments> generateData() {
        return Stream.of(
                Arguments.of(
                        getOptions(),
                        "Oracel Certifed Profesionaler Java Programer",
                        "Oracle Certified Professional Java Programmer"
                ),
                Arguments.of(
                        getOptions(),
                        "Oracle Certified Profess Java Programmer",
                        "Oracle Certified Professional Java Programmer"
                ),
                Arguments.of(
                        getOptions(),
                        "Sum Certified Enterpreis Architekt",
                        "Sun Certified Enterprise Architect"
                ),
                Arguments.of(
                        getOptions(),
                        "foundation: Certified Profesional für Software Architektur",
                        "Foundation Level: Certified Professional for Software Architecture"
                ),
                Arguments.of(
                        getOptions(),
                        "Kutomers Experiences Faciilitator",
                        "Customer Experience Facilitator"
                ),
                Arguments.of(
                        getOptions(),
                        "UX Fundation Levle",
                        "UXQB Foundation Level"
                ),
                Arguments.of(
                        getOptions(),
                        "SaFe Agilist Ausbildung",
                        "SAFe Agilist"
                ),
                Arguments.of(
                        getOptions(),
                        "GitLab 301",
                        "GitLab 101"
                ) // sött eig e error throwe
        );
    }

    private static List<String> getOptions() {
        return Arrays.asList(
                "Securing the AI/ML Supply Chain",
                "Chainguard Containers Onboarding Guide",
                "Painless Vulnerability Management",
                "Architecting on Amazon Web Services",
                "Advanced Architecting on Amazon Web Services",
                "GitLab Security Essentials Certification",
                "GitLab 101",
                "GitLab 201",
                "GitLab Certified Git Associate",
                "GitLab Certified Migration Services Certification",
                "GitLab CI/CD Certification",
                "GitLab Continuous Integration Sales Speciality",
                "GitLab DevSecOps Sales Specialist",
                "GitLab Project Management Certification",
                "GitLab Sales Core",
                "GitLab Solutions Architect Core",
                "Automating Supply Chain Security: SBOMs and Signatures",
                "Introduction to Cloud Infrastructure Technologies",
                "Introduction to GitOps",
                "Red Hat Certified Specialist in Hybrid Cloud Management",
                "Red Hat Certified Developer in Cloud-native Applications (EX188)",
                "Red Hat Certified JBoss Developer (RHCJD)",
                "Red Hat Certified Specialist in Enterprise Application Server Administration (RHCJA)",
                "Red Hat Certified Specialist in Configuration Management",
                "Red Hat Certified Architect in Infrastructure",
                "Sun Certified System Administrator ",
                "Green Software for Practitioners",
                "Certified Safe 5 Practitioner",
                "Cisco Sales Specialist for Unified Computing Technology",
                "ITIL Advanced Certification (Version 2.0)",
                "Foundation Certificate in IT Service Management (ITIL) v2.0",
                "VMware Technical Sales Professional (VTSP)",
                "Sun Certified Solaris Administrator (UNIX)",
                "HP Advanced System Engineer (ASE) – Storageworks",
                "Veritas Certified Professional High Availability (VCPHA)",
                "EMC Storage Technologist (EMCST) – Specialist",
                "EMC Proven Professional – Technology Architect (EMCTA – Storage Infrastructre)",
                "EMC Proven Professional – NAS Technology Architect (NAS TA)",
                "Beihaltet alle drei (Onboarding Guide, AI/ML Guardian, Vulnslayer)",
                "GitOps at Scale",
                "GitOps Fundamentals",
                "Confluent Certified Administrator for Apache Kafka",
                "Confluent Certified Developer for Apache Kafka",
                "Convolutional Neural Networks",
                "Neural Networks and Deep Learning",
                "Improving Deep Neural Networks: Hyperparameter tuning, Regularization and Optimization",
                "Structuring Machine Learning Projects ",
                "Docker Certified Associate",
                "DevOps Kubernetes Camp",
                "Google Cloud Certification – Foundational Level",
                "Google Cloud Digital Leader – Foundational Level",
                "Requirement for Designing Strategy ",
                "Customer Experience Facilitator",
                "Certified Agil Tester 05 CAT",
                "Certified Jenkins Engineer",
                "Linbit Certified Architect",
                "Certified Backstage Associate",
                "Certified GitOps Associate",
                "Introduction to Cilium",
                "Monitoring Systems and Services with Prometheus",
                "Containers Fundamentals",
                "GitOps: Continuous Delivery on Kubernetes with Flux",
                "LPIC-1: Linux Administrator",
                "LPIC-2: Linux Engineer",
                "LPIC-3: Enterprise Professional Mixed Environment",
                "LPIC-3: Enterprise Professional Security",
                "LPIC-3: Enterprise Professional Virtualization & HA",
                "Microsoft Certified Azure Developer – old",
                "Azure Certification – Fundamentals Level",
                "Nielsen Norman Group UX Certificate",
                "OpenTelemetry Certified Associate ",
                "Oracle Certified Associate, Java SE 8 Programmer",
                "Oracle Certified Java Foundations",
                "Oracle Linux Certified Implementation Specialist",
                "Certified Associate in Python Programming",
                "Certified Entry-Level Python Programmer",
                "Certified Professional in Python Programming",
                "Certified Rancher Operator: Level 1 (wird ersetzt durch SUSE Rancher 2.5)",
                "Red Hat Certificate of Expertise in PaaS (Ist der Vorgänger von Red Hat Certified Specialist in OpenShift Administration)",
                "UXQB Foundation Level",
                "Advanced Certified Scrum Product Owner",
                "Suse Certified Administrator",
                "Certified Cloud Native Platform Engineering Associate",
                "Kubernetes and Cloud Native Associate ",
                "Togfa 9 Certified",
                "HERMES 5 Advanced",
                "HERMES 5 Foundation",
                "KI-Agenten & Automationen ohne Code",
                "Usability Expert",
                "Certified Professional for Usability and User Experience – Foundation Level",
                "WAI0.1x: Introduction to Web Accessibility",
                "Berufsbildner um Lernende auszubilden",
                "Deep Learning Spezialisierung",
                "Certified Prof. for Requirements Engineering (mit Prüfung)",
                "Certified Prof. for Requirements Engineering (mit Prüfung) Fountaion Level",
                "Machine Learning Stanford Online",
                "Professional Scrum Master I",
                "Professional Scrum Product Owner I",
                "Leading Safe",
                "SAFe Agilist",
                "SAFe Program Consultant",
                "Certified Spring Professional",
                "TensorFlow in Practice",
                "Cisco Certified Network Associate",
                "Certified Kubernetes Security Specialist",
                "Accredited Partner Technical Engineer",
                "Certified Services Engineer Professional",
                "Google Cloud Certification – Associate Level",
                "Google Cloud Certified Associate Cloud Engineer",
                "Google Cloud Certification – Professional Level",
                "Certified Project Manager",
                "Certified Project Management Associate",
                "Behavioral Design Bootcamp",
                "Linbit Certified Engineer",
                "Certified Kubernetes Administrator",
                "Certified Kubernetes Application Developer",
                "Linux Foundation Certified Engineer",
                "Linux Foundation Certified System Administrator",
                "Prometheus Certified Associate",
                "Azure Certification Azure Developer – Associate Level",
                "Azure Certification – Associate Level",
                "Azure Certification – Professional Level",
                "Oracle Certified Professional: Java SE 17 Developer",
                "Certified Specialist in Containers and Kubernets",
                "Red Hat Certified Enterprise Application Developer (RHCEAD)",
                "Red Hat Certified System Administrator (RHCSA)",
                "Red Hat Certified System Administrator in Red Hat OpenStack",
                "Red Hat Certified Specialist in Gluster Storage Administration",
                "Red Hat Certified Specialist in OpenShift Administration",
                "Red Hat Certified Enterprise Microservices Developer (RHCEMD)",
                "Red Hat Certified Specialist in OpenShift Application Development",
                "Red Hat Certified Engineer (RHCE) for Red Hat Enterprise Linux 8",
                "Red Hat Certified Engineer (RHCE) for Red Hat Enterprise Linux 7",
                "Red Hat Certified Specialist in Virtualization",
                "Red Hat Certified Specialist in Linux Diagnostics and Troubleshooting",
                "Red Hat Certified Specialist in Developing Automation with Ansible Automation Platform",
                "Red Hat Certified Specialist in Deployment and Systems Management",
                "Red Hat Certified Specialist in Ansible Automation",
                "Red Hat Certified Specialist in Microsoft Windows Automation with Ansible",
                "Red Hat Certified Specialist in Security: Containers and OpenShift Container Platform",
                "Red Hat Certified Specialist in High Availability Clustering",
                "Red Hat Certified Specialist in Ansible Best Practices",
                "Red Hat Certified Specialist in Managing Automation with Ansible Automation Platform",
                "Red Hat Certified Specialist in OpenShift AI (EX267)",
                "Red Hat Certificate of Expertise in Deployment and System Managements ",
                "Red Hat Certificate of Expertise in Server Hardening",
                "SUSE Certified Linux Administrator in Enterprise Linux",
                "Certified Professional for Usability and User Experience – User Requirements Engineering",
                "Certified Professional for Usability and User Experience – Usability Testing and Evaluation",
                "CompTIA Network,",
                "GIAC Certified Forensic Analyst",
                "GIAC Certified Incident Handler",
                "Foundation Level: Certified Professional for Software Architecture",
                "Certified Tester (SAQ)",
                "Scaled Agile Framework",
                "Cisco Certified Network Professional",
                "Professional Diploma in UX Design",
                "Certified Information Security Professional",
                "Advanced Level (3 Module & Zertifikat) Die verschiedene Module sind hier: https://www.isaqb.org/certifications/advanced-level/",
                "Sun Certified Enterprise Architect",
                "Oracle Certified Master Java Developer",
                "Oracle Certified Professional Java Programmer",
                "OSSTMM Professional Security Tester",
                "Certified Argo Project Associate",
                "Red Hat Certified Specialist in Services Management and",
                "Kubernetes and Cloud Native Security Associate",
                "AWS Certified Cloud Practitioner (CLF-C02)   AWS Certified AI Practitioner (AIF-C01)",
                "AWS Certified Solutions Architect – Associate (SAA-C03)   AWS Certified Developer – Associate (DVA-C02)   AWS Certified CloudOps Engineer – Associate (SOA-C03)   AWS Certified Data Engineer – Associate (DEA-C01)   AWS Certified Machine Learning Engineer – Associate (MLA-C01)",
                "AWS Certified Solutions Architect – Professional (SAP-C02)   AWS Certified DevOps Engineer – Professional (DOP-C02)   AWS Certified Generative AI Developer – Professional (AIP-C01)",
                "AWS Certified Security – Specialty (SCS-C03)   AWS Certified Advanced Networking – Specialty (ANS-C01)"
        );
    }
}


