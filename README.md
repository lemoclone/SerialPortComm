Serial port library
===================
Serial port communication library for Android

## Setup
Add the library as aar dependency to your build.

**Gradle**

    compile 'com.lexiang.library:serialcomm:1.0.1'

## Usage

Open serial port and start reader, writer queue

        //set up serial port
        SerialPortStrategy serialPortStrategy = new SerialPortStrategy();
        serialPortStrategy.setSerialPortBaudRate(Constants.SERIAL_BAUD_RATE_4800);
        serialPortStrategy.setSerialPortDataBits(Constants.DATABITS_8);
        serialPortStrategy.setSerialPortParity(Constants.PARITY_NONE);
        serialPortStrategy.setSerialPortStopBits(Constants.STOPBITS_1);
        //set the read interval time
        serialPortStrategy.setSerialPortReaderIntervalTimeInMillis(100);
        //set the read buffer size
        serialPortStrategy.setInputStreamSizeInByte(128);
        //set the device port path
        serialPortStrategy.setSerialPortPath("/dev/ttyS2");

        //create a dataHandler for read data from serial port
        DataHandler readerHandler = new DataHandler() {
            @Override
            public void handleData(SerialPortData serialPortData) throws TRMError {
                if (serialPortData.getDataBytes() != null) {
                    Log.i("IncomeDataHandler2", ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                }
            }
        };

        //create a dataHandler for write data into serial port
        DataHandler writerHandler = new DataHandler() {
            @Override
            public void handleData(SerialPortData serialPortData) throws TRMError {

                if (SerialPortClient.getInstance().writeData(serialPortData.getDataBytes()) > -1) {
                    if (serialPortData.getDataHandleListener() != null) {
                        serialPortData.getDataHandleListener().onSucceed(ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                    }
                } else {
                    if (serialPortData.getDataHandleListener() != null) {
                        serialPortData.getDataHandleListener().onFailed(ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                    }
                }
            }
        };

        //init SerialPortReader
        SerialPortReader.init(readerHandler);
        //init SerialPortWriter
        SerialPortWriter.init(writerHandler);
        //open serial port
        SerialPortClient.getInstance().start(serialPortStrategy,new ByteSerialPortReaderListener());

Custom the SerialPortListener

        SerialPortReaderListener serialPortReaderListener = new SerialPortReaderListener() {
            @Override
            public void onDataChanged(byte[] data) {
                //do sth whatever you'll do,such as byte decode digester
            }
        };
       SerialPortClient.getInstance().start(serialPortStrategy,serialPortReaderListener);


## examples
see demo
