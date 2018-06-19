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
               SerialPortParams serialPortParams = new SerialPortParams.Builder()
                       .serialPortBaudRate(Constants.SERIAL_BAUD_RATE_4800)
                       .serialPortDataBits(Constants.DATABITS_8)
                       .serialPortParity(Constants.PARITY_NONE)
                       .serialPortStopBits(Constants.STOPBITS_1)
                       .serialPortReaderIntervalTimeInMillis(10)  //set the read interval time
                       .inputStreamSizeInByte(128)  //set the read buffer size
                       .serialPortPath("/dev/ttyO3").build(); //set the device port path

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
