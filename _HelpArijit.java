import java.nio.ByteBuffer;
import com.google.protobuf.ByteString;
import com.hedera.hashgraph.sdk.contract.ContractFunctionResult;
import com.hedera.hashgraph.sdk.TransactionId;
import com.hedera.hashgraph.sdk.contract.ContractExecuteTransaction;
import com.hedera.hashgraph.sdk.contract.ContractId;
import com.hedera.hashgraph.sdk.Status;
import net.aoc.JCoHelper;

public class _HelpArijit {
    private static ByteString rawResult = null;
    public _HelpArijit() {
    	
    }
	public static void main(String... arguments) throws Exception {

		var IssuerId = JCoHelper.getOperatorId();
		var client = JCoHelper.createHederaClient().setMaxTransactionFee(834_000_000);
		String memoField = "Help Arijit";

		//ContractId stringContract = new ContractId(0, 0, 46020);
		//ContractId stringContract = new ContractId(0, 0, 46976);
		ContractId stringContract = new ContractId(0, 0, 46972);
		System.out.println("The Contract is          " + stringContract);

		if (stringContract != null) {
			long cost = 150_000_000;
			TransactionId txId = new TransactionId(IssuerId);
			var contractTx = new ContractExecuteTransaction().setTransactionId(txId).setGas(50_400)
					.setTransactionMemo(memoField).setContractId(stringContract).setFunction("readStringArray");
			var contractReceipt = contractTx.execute(client).getReceipt(client);
			if (contractReceipt.status != Status.Success) {
				System.out.println("Error calling contract: " + contractReceipt.status);
				return;
			}
			ContractFunctionResult contractResult = txId.getRecord(client).getContractExecuteResult();
			
			rawResult = ByteString.copyFrom(contractResult.asBytes());
			
			//Now we work on the String Array reader.
			String[] contractStringArray = getStringArray(0);
			System.out.println(contractStringArray);
		}
	}
    public static String[] getStringArray(int valIndex) {
    	int offset = getIntValueAt(valIndex * 32);
        int arrayLength = getIntValueAt(offset);
        int[] offsetForEntry = new int[arrayLength];
        int[] lengthPerEntry = new int[arrayLength];
        String[] stringPerEntry = new String[arrayLength];
        offset += 32 + arrayLength * 32;
        for (int i = 0; i < arrayLength; i++ ) {        
        	offsetForEntry[i] = offset;
        	System.out.println("Offset " + offsetForEntry[i]);
        	lengthPerEntry[i] = getIntValueAt(offsetForEntry[i]);
        	System.out.println("Length " + lengthPerEntry[i]);
        	offset += 32;
        	stringPerEntry[i] = getStringAt(offset,lengthPerEntry[i]);
        	System.out.println("String " + stringPerEntry[i]);
        	int mod32 = 32 - lengthPerEntry[i] % 32;
        	offset += lengthPerEntry[i] + mod32;
        }
        
		return stringPerEntry;
	}
        
    private static int getIntValueAt(int valueOffset) {
        return getByteBuffer(valueOffset + 28).getInt();
    }
    private static ByteBuffer getByteBuffer(int offset) {
        ByteBuffer byteBuffer = rawResult.asReadOnlyByteBuffer();
        byteBuffer.position(byteBuffer.position() + offset);
        return byteBuffer;
    }
    private static ByteString getByteString(int startIndex, int endIndex) {
        return rawResult.substring(startIndex, endIndex);
    }
    public static String getStringAt(int valIndex, int len) {
        return getByteString(valIndex, valIndex + len).toStringUtf8();
    }    
}
