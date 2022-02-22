package tv.accedo.control.model

data class ApplicationStatus(val status: Status, val message: String) {
    enum class Status {
        ACTIVE, MAINTENANCE, UNKNOWN
    }
}