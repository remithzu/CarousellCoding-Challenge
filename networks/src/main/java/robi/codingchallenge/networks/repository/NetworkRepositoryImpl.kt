package robi.codingchallenge.networks.repository

import robi.codingchallenge.networks.NetworkApi
import javax.inject.Inject

class NetworkRepositoryImpl @Inject constructor(
    api: NetworkApi
) : NetworkRepository(api)