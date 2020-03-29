package com.szarpcode.samples;

import java.util.stream.IntStream;

public class SynchronizedMethods {

  private static StringChanger stringChanger = new StringChanger();

  public static void main(String[] args) throws InterruptedException {

    System.out.println("------without synchronization: ------");

    Thread thread1 = new Thread(iteration(stringChanger::changeStringToA));
    Thread thread2 = new Thread(iteration(stringChanger::changeStringToB));

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    System.out.println("------with synchronization: ------");

    Thread thread3 = new Thread(iteration(stringChanger::synchronizedChangeStringToA));
    Thread thread4 = new Thread(iteration(stringChanger::synchronizedChangeStringToB));

    thread3.start();
    thread4.start();
  }

  private static Runnable iteration(Runnable changeStringAction) {
    return () -> IntStream.rangeClosed(0, 5).forEach(count -> changeStringAction.run());
  }

  static class StringChanger {

    private String name = "John";

    void changeStringToA() {
      sleep();
      System.out.println("Changing name to: A");
      this.name = "A";
    }

    synchronized void synchronizedChangeStringToA() {
      changeStringToA();
    }

    void changeStringToB() {
      sleep();
      System.out.println("Changing name to: B");
      this.name = "B";
    }

    synchronized void synchronizedChangeStringToB() {
      changeStringToB();
    }

    private void sleep() {
      try {
        Thread.sleep(50);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }
  }
}
