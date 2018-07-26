#Android 串口库

## Setup
添加 jitpack 依赖
[![](https://jitpack.io/v/lemoclone/SerialPortComm.svg)](https://jitpack.io/#lemoclone/SerialPortComm)


## Usage

打开串口读写库

        //设置串口参数
               SerialPortParams serialPortParams = new SerialPortParams.Builder()
                       .serialPortBaudRate(Constants.SERIAL_BAUD_RATE_4800)
                       .serialPortDataBits(Constants.DATABITS_8)
                       .serialPortParity(Constants.PARITY_NONE)
                       .serialPortStopBits(Constants.STOPBITS_1)
                       .serialPortReaderIntervalTimeInMillis(10)  //set the read interval time
                       .inputStreamSizeInByte(128)  //set the read buffer size
                       .serialPortPath("/dev/ttyO3").build(); //set the device port path

        //处理串口读
        DataHandler readerHandler = new DataHandler() {
            @Override
            public void handleData(SerialPortData serialPortData) throws TRMError {
                if (serialPortData.getDataBytes() != null) {
                    Log.i("IncomeDataHandler2", ByteUtil.toDisplayString(serialPortData.getDataBytes()));
                }
            }
        };

        //处理串口写
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

        //初始化
        SerialPortReader.init(readerHandler);
        //初始化
        SerialPortWriter.init(writerHandler);
        //打开串口
        SerialPortClient.getInstance().start(serialPortStrategy,new ByteSerialPortReaderListener());

自定义串口监听SerialPortListener

        SerialPortReaderListener serialPortReaderListener = new SerialPortReaderListener() {
            @Override
            public void onDataChanged(byte[] data) {
                //do sth whatever you'll do,such as byte decode digester
            }
        };
       SerialPortClient.getInstance().start(serialPortStrategy,serialPortReaderListener);


## examples
请查看demo
