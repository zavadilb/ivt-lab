package hu.bme.mit.spaceship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class GT4500Test {

  private GT4500 ship;
  private TorpedoStore pTS = Mockito.mock(TorpedoStore.class);
  private TorpedoStore sTS = Mockito.mock(TorpedoStore.class);

  @BeforeEach
  public void init(){
    this.ship = new GT4500();
    this.ship.setPrimaryTorpedoStore(pTS);
    this.ship.setSecondaryTorpedoStore(sTS);
  }

  @Test
  public void fireTorpedo_Single_Success(){
    // Arrange
    when(pTS.fire(1)).thenReturn(true);
    when(sTS.fire(1)).thenReturn(true);
    
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
  }

  @Test
  public void fireTorpedo_All_Success(){
    // Arrange
    when(pTS.fire(1)).thenReturn(true);
    when(sTS.fire(1)).thenReturn(true);

    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
  }

  // Test 1: check if ALL torpedo stores are fired with ALL firing mode.
  @Test
  public void fireTorpedo_All_All_Fired() {
    // Arrange
    when(pTS.fire(1)).thenReturn(true);
    when(sTS.fire(1)).thenReturn(true);
  
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(true, result);
    verify(pTS, times(1)).fire(1);
    verify(sTS, times(1)).fire(1);
  }
  // Test 2: check if for the first time the primary store is fired.
  @Test
  public void fireTorpedo_Single_First_Fires_Primary() {
    // Arrange
    when(pTS.fire(1)).thenReturn(true);
  
    // Act
    boolean result = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result);
    verify(pTS, times(1)).fire(1);
    verify(sTS, times(0)).fire(1);
  }
  // Test 3: check if the torpedo stores are alternating
  @Test
  public void fireTorpedo_Single_Alternates() {
    // Arrange
    when(pTS.fire(1)).thenReturn(true);
    when(sTS.fire(1)).thenReturn(true);
  
    for (int i = 0; i < 10; i++) {
      boolean result = ship.fireTorpedo(FiringMode.SINGLE);
      assertEquals(true, result);
    }

    verify(pTS, times(5)).fire(1);
    verify(sTS, times(5)).fire(1);
  }
  // Test 4: Check if the next one in line is empty, the ship tries to fire the other one
  @Test
  public void fireTorpedo_Single_Switches_To_Next_On_Empty() {
    // Arrange
    when(pTS.isEmpty()).thenReturn(true);
    when(pTS.getTorpedoCount()).thenReturn(0);
    when(sTS.fire(1)).thenReturn(true);
  
    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(true, result1);
    verify(pTS, times(0)).fire(1);
    verify(sTS, times(1)).fire(1);
  }

  // Test 5: Check if the fired store reports a failure, the ship does not fire the other one
  @Test
  public void fireTorpedo_Single_No_Switch_To_Next_On_Failure() {
    // Arrange
    when(pTS.isEmpty()).thenReturn(false);
    when(pTS.fire(1)).thenReturn(false);
    when(sTS.fire(1)).thenReturn(true);
  
    // Act
    boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);

    // Assert
    assertEquals(false, result1);
    verify(pTS, times(1)).fire(1);
    verify(sTS, times(0)).fire(1);
  }

  // Test 6: Source code only
  @Test 
  public void fireTorpedo_All_Both_Fail() {
    // Arrange
    when(pTS.fire(1)).thenReturn(false);
    when(sTS.fire(1)).thenReturn(false);
  
    // Act
    boolean result = ship.fireTorpedo(FiringMode.ALL);

    // Assert
    assertEquals(false, result);
    verify(pTS, times(1)).fire(1);
    verify(sTS, times(1)).fire(1);
  }

    // Test 5: Check if the fired store reports a failure, the ship does not fire the other one
    @Test
    public void fireTorpedo_Single_Do_Not_Alternate_On_Secondar() {
      // Arrange
      when(pTS.isEmpty()).thenReturn(false);
      when(sTS.isEmpty()).thenReturn(true);
      when(pTS.fire(1)).thenReturn(true);
      when(sTS.fire(1)).thenReturn(true);
    
      // Act
      boolean result1 = ship.fireTorpedo(FiringMode.SINGLE);
      boolean result2 = ship.fireTorpedo(FiringMode.SINGLE);
      boolean result3 = ship.fireTorpedo(FiringMode.SINGLE);
  
      // Assert
      assertEquals(true, result1);
      assertEquals(true, result2);
      assertEquals(true, result3);
      verify(pTS, times(3)).fire(1);
      verify(sTS, times(0)).fire(1);
    }
}
