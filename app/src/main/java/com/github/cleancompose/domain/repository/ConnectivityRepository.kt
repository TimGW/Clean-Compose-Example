package com.github.cleancompose.domain.repository

import kotlinx.coroutines.flow.Flow

/** Repository for interacting with network status */
interface ConnectivityRepository {

    /**
     * Get the current network status
     *
     * @return Flow containing true is network is available
     */
    val connectionState: Flow<Boolean>
}
