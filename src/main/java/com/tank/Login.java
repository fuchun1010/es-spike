package com.tank;

import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.val;

import java.net.URISyntaxException;

/**
 * @author tank198435163.com
 */
public class Login {
  public static void main(String[] args) throws URISyntaxException {
    val socket = IO.socket("http://localhost:9093");
    socket.on(Socket.EVENT_CONNECT, objects -> System.out.println("========"));
    socket.connect();
  }
}
