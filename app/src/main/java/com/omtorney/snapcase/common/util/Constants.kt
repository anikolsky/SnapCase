package com.omtorney.snapcase.common.util

object Constants {

    const val DATA_STORE_NAME = "SnapCase_Preferences_DataStore"
    const val INITIAL_COLOR = 0xFF6892B0

    const val NOTIFICATION_CHANNEL_ID = "snapcase_channel_id"
    const val NOTIFICATION_CHANNEL_NAME = "snapcase_channel"
    const val NOTIFICATION_CHANNEL_DESCRIPTION = "Snapcase Channel"
    const val NOTIFICATION_ID = 1000

    const val WORKER_UNIQUE_ONETIME_WORK_NAME = "onetime_work" // TODO change name
    const val WORKER_UNIQUE_PERIODIC_WORK_NAME = "periodic_case_check_work"
    const val WORKER_RESULT_KEY = "snapcase_work_result_key"
    const val WORKER_ERROR_MSG_KEY = "error_message"
    const val WORK_ONETIME_INTERVAL = 3L
    const val WORK_REPEAT_INTERVAL = 60L
}