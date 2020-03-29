package com.szarpcode.samples;

public class WaitAndNotify {

  public static void main(String[] args) {
    Channel channel = new Channel();
    Thread thread1 = new Thread(() -> channel.push("A"));
    Thread thread2 = new Thread(() -> channel.push("B"));
    Thread thread3 = new Thread(() -> channel.push("C"));

    Thread thread4 = new Thread(() -> System.out.println("pull message: " + channel.pull()));
    Thread thread5 = new Thread(() -> System.out.println("pull message: " + channel.pull()));
    Thread thread6 = new Thread(() -> System.out.println("pull message: " + channel.pull()));


    thread1.start();
    thread2.start();
    thread3.start();
    thread4.start();
    thread5.start();
    thread6.start();
  }

  private static class Channel {

    private String message = null;

    synchronized void push(String message) {
      if (!isChannelEmpty()) {
        waitForPull();
      }
      System.out.println("push message: " + message);
      this.message = message;
    }

    synchronized String pull() {
      try {
        if (isChannelEmpty()) {
          waitForPull();
        }
        return message;
      } finally {
        this.message = null;
        notify();
      }
    }

    synchronized private boolean isChannelEmpty() {
      return message == null;
    }

    void waitForPull() {
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }

  }

}
