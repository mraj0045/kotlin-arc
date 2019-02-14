package com.arc.kotlin.api.security

import java.io.IOException
import java.net.InetAddress
import java.net.Socket

import javax.net.ssl.SSLSocket
import javax.net.ssl.SSLSocketFactory

class CustomSSLSocketFactory : SSLSocketFactory() {

    private var socketFactory = SSLSocketFactory.getDefault() as SSLSocketFactory

    override fun getDefaultCipherSuites(): Array<String> {
        return socketFactory.defaultCipherSuites
    }

    override fun getSupportedCipherSuites(): Array<String> {
        return socketFactory.supportedCipherSuites
    }

    @Throws(IOException::class)
    override fun createSocket(s: Socket, host: String, port: Int, autoClose: Boolean): Socket {
        val socket = socketFactory.createSocket(s, host, port, autoClose) as SSLSocket
        socket.enabledProtocols = socket.supportedProtocols
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int): Socket {
        val socket = socketFactory.createSocket(host, port) as SSLSocket
        socket.enabledProtocols = socket.supportedProtocols
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(host: String, port: Int, localHost: InetAddress, localPort: Int): Socket {
        val socket = socketFactory.createSocket(host, port, localHost, localPort) as SSLSocket
        socket.enabledProtocols = socket.supportedProtocols
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(host: InetAddress, port: Int): Socket {
        val socket = socketFactory.createSocket(host, port) as SSLSocket
        socket.enabledProtocols = socket.supportedProtocols
        return socket
    }

    @Throws(IOException::class)
    override fun createSocket(address: InetAddress, port: Int, localAddress: InetAddress, localPort: Int): Socket {
        val socket = socketFactory.createSocket(address, port, localAddress, localPort) as SSLSocket
        socket.enabledProtocols = socket.supportedProtocols
        return socket
    }
}