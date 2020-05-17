pragma solidity 0.5.3;
pragma experimental ABIEncoderV2;

contract StringArrays {
    string[] users;
    constructor ( ) public {
    }
    function readStringArray() public returns ( string[] memory  _stringArray ){
      delete users;
      users.push( 'cat' );
      users.push( 'dog' );
      users.push( 'pig' );
      users.push( 'horse' );
      users.push( 'rat' );
      return users;
    }
    function uintToString(uint v) internal returns (string memory str) {
        uint maxlength = 100;
        bytes memory reversed = new bytes(maxlength);
        uint i = 0;
        while (v != 0) {
            uint remainder = v % 10;
            v = v / 10;
            reversed[i++] = byte(uint8(48 + remainder));
        }
        bytes memory s = new bytes(i + 1);
        for (uint j = 0; j <= i; j++) {
            s[j] = reversed[i - j];
        }
        str = string(s);
    }    
}