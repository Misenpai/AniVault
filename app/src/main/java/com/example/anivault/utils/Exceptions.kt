package com.example.anivault.utils

import java.io.IOException

class ApiException(message: String) : IOException(message)
class NoInternetException(message: String) : IOException(message)
class TimeoutException(message: String) : IOException(message)