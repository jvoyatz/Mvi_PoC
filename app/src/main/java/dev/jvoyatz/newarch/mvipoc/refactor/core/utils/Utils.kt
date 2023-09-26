package dev.jvoyatz.newarch.mvipoc.refactor.core.utils

import timber.log.Timber


fun logThread() = Timber.e("Current thread --> ${Thread.currentThread().name}")