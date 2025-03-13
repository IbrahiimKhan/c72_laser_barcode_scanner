import React, { useCallback, useEffect, useState } from 'react';
import { NativeEventEmitter, NativeModules, Text, View, StyleSheet, ScrollView } from 'react-native';
import KeyEvent from 'react-native-keyevent';

interface BarcodeData {
  barcode: string;
}

const { BarcodeScannerModule } = NativeModules;

const App: React.FC = () => {
  const [barcodes, setBarcodes] = useState<BarcodeData[]>([]);
  const handleBarcodeScanned = useCallback((scannedData: BarcodeData) => {
    setBarcodes((prevBarcodes) => [...prevBarcodes, scannedData]);
  }, []);

  const handleKeyDown = useCallback((keyEvent: { keyCode: number }) => {
    const triggerKeyCodes = [139, 280, 293];
    if (triggerKeyCodes.includes(keyEvent.keyCode)) {
      BarcodeScannerModule.startScan();
    }
  }, []);

  useEffect(() => {
    BarcodeScannerModule.openScanner();
    const eventEmitter = new NativeEventEmitter(BarcodeScannerModule);

    const subscription = eventEmitter.addListener(
      'onBarcodeScanned',
      handleBarcodeScanned
    );
    KeyEvent.onKeyDownListener(handleKeyDown);

    return () => {
      BarcodeScannerModule.closeScanner();
      KeyEvent.removeKeyDownListener();
      subscription.remove();
    };
  }, [handleBarcodeScanned, handleKeyDown]);

  return (
    <View style={styles.container}>
     <Text style={styles.header}>Scanned Barcodes:</Text>
     <ScrollView showsVerticalScrollIndicator={false}>
      {barcodes.map((barcode, index) => (
        <Text key={index} style={styles.barcodeText}>
          {barcode}
        </Text>
      ))}
     </ScrollView>
    </View>
  );
};

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    padding: 20,
  },
  header: {
    fontSize: 20,
    fontWeight: 'bold',
    marginBottom: 10,
  },
  barcodeText: {
    fontSize: 16,
    color: 'black',
    marginTop: 5,
  },
});

export default App;
