/*
 * This file was automatically generated by EvoSuite
 * Mon Jan 11 23:37:16 GMT 2021
 */


import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.util.HashMap;
import java.util.List;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class) @EvoRunnerParameters(mockJVMNonDeterminism = true, useVFS = true, useVNET = true, resetStaticState = true, separateClassLoader = true) 
public class cMelhorCusto_ESTest extends cMelhorCusto_ESTest_scaffolding {

  @Test(timeout = 4000)
  public void test0()  throws Throwable  {
      cMelhorCusto cMelhorCusto0 = new cMelhorCusto();
      // Undeclared exception!
      try { 
        cMelhorCusto0.compare((PlataformaEntrega) null, (PlataformaEntrega) null);
        fail("Expecting exception: NullPointerException");
      
      } catch(NullPointerException e) {
         //
         // no message in exception (getMessage() returned null)
         //
         verifyException("cMelhorCusto", e);
      }
  }

  @Test(timeout = 4000)
  public void test1()  throws Throwable  {
      cMelhorCusto cMelhorCusto0 = new cMelhorCusto();
      Voluntarios voluntarios0 = new Voluntarios();
      // Undeclared exception!
      try { 
        cMelhorCusto0.compare((PlataformaEntrega) voluntarios0, (PlataformaEntrega) voluntarios0);
        fail("Expecting exception: ClassCastException");
      
      } catch(ClassCastException e) {
         //
         // class Voluntarios cannot be cast to class Transportadoras (Voluntarios and Transportadoras are in unnamed module of loader org.evosuite.instrumentation.InstrumentingClassLoader @4d400df1)
         //
         verifyException("cMelhorCusto", e);
      }
  }

  @Test(timeout = 4000)
  public void test2()  throws Throwable  {
      cMelhorCusto cMelhorCusto0 = new cMelhorCusto();
      Transportadoras transportadoras0 = new Transportadoras();
      Ponto ponto0 = new Ponto(1859.3636F, 0);
      HashMap<String, Encomenda> hashMap0 = new HashMap<String, Encomenda>();
      Transportadoras transportadoras1 = new Transportadoras((String) null, (String) null, "", ponto0, 1.0, false, true, (List<Integer>) null, hashMap0, "P_5 ", (-1538.3), 2126);
      int int0 = cMelhorCusto0.compare((PlataformaEntrega) transportadoras0, (PlataformaEntrega) transportadoras1);
      assertEquals(1, int0);
  }

  @Test(timeout = 4000)
  public void test3()  throws Throwable  {
      cMelhorCusto cMelhorCusto0 = new cMelhorCusto();
      Transportadoras transportadoras0 = new Transportadoras();
      Ponto ponto0 = new Ponto(1859.3636F, 0);
      HashMap<String, Encomenda> hashMap0 = new HashMap<String, Encomenda>();
      Transportadoras transportadoras1 = new Transportadoras((String) null, (String) null, "", ponto0, 1.0, false, true, (List<Integer>) null, hashMap0, "P_5 ", (-1538.3), 2126);
      int int0 = cMelhorCusto0.compare((PlataformaEntrega) transportadoras1, (PlataformaEntrega) transportadoras0);
      assertEquals((-1), int0);
  }

  @Test(timeout = 4000)
  public void test4()  throws Throwable  {
      cMelhorCusto cMelhorCusto0 = new cMelhorCusto();
      Transportadoras transportadoras0 = new Transportadoras();
      int int0 = cMelhorCusto0.compare((PlataformaEntrega) transportadoras0, (PlataformaEntrega) transportadoras0);
      assertEquals(0, int0);
  }
}