/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 11 23:38:57 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.NoSuchElementException;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.util.SystemInUtil;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class Input_ESTest extends Input_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test00()  throws Throwable  {
      SystemInUtil.addInputLine("[");
      // Undeclared exception!
      try { 
        Input.lerShort();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test01()  throws Throwable  {
      SystemInUtil.addInputLine("BC\"F+V");
      // Undeclared exception!
      try { 
        Input.lerInt();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test02()  throws Throwable  {
      SystemInUtil.addInputLine("Inteiro Invalido");
      // Undeclared exception!
      try { 
        Input.lerFloat();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test03()  throws Throwable  {
      SystemInUtil.addInputLine("$1MSj_e_;pZz=");
      // Undeclared exception!
      try { 
        Input.lerDouble();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test04()  throws Throwable  {
      SystemInUtil.addInputLine(".9");
      // Undeclared exception!
      try { 
        Input.lerBoolean();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test05()  throws Throwable  {
      SystemInUtil.addInputLine("");
      String string0 = Input.lerString();
      assertEquals("", string0);
  }

  @Test(timeout = 4000)
  public void test06()  throws Throwable  {
      // Undeclared exception!
      try { 
        Input.lerString();
        fail("Expecting exception: NoSuchElementException");
      
      } catch(NoSuchElementException e) {
         //
         // No line found
         //
         verifyException("java.util.Scanner", e);
      }
  }

  @Test(timeout = 4000)
  public void test07()  throws Throwable  {
      SystemInUtil.addInputLine("9");
      short short0 = Input.lerShort();
      assertEquals((short)9, short0);
  }

  @Test(timeout = 4000)
  public void test08()  throws Throwable  {
      SystemInUtil.addInputLine("9 FlKyHOOHm");
      float float0 = Input.lerFloat();
      assertEquals(9.0F, float0, 0.01F);
  }

  @Test(timeout = 4000)
  public void test09()  throws Throwable  {
      SystemInUtil.addInputLine("3");
      double double0 = Input.lerDouble();
      assertEquals(3.0, double0, 0.01);
  }

  @Test(timeout = 4000)
  public void test10()  throws Throwable  {
      SystemInUtil.addInputLine("3");
      int int0 = Input.lerInt();
      assertEquals(3, int0);
  }

  @Test(timeout = 4000)
  public void test11()  throws Throwable  {
      SystemInUtil.addInputLine(";Vo");
      String string0 = Input.lerString();
      assertEquals(";Vo", string0);
  }

  @Test(timeout = 4000)
  public void test12()  throws Throwable  {
      Input input0 = new Input();
  }
}