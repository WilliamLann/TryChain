pragma solidity ^0.4.21;

/**
 *@dev https://github.com/ethereum/EIPs/issues/20
 */
contract StandardToken {
  address public organizer;
  string public constant name = "TryBlockChain Token";
  string public constant symbol = "TBC";
  uint8 public constant decimals = 0;
  uint256 totalSupply_ = 210000000;
  mapping (address => mapping (address => uint256)) internal allowed;
  mapping(address => uint256) balances;
  event Transfer(address indexed from, address indexed to, uint256 value);
  event Approval(address indexed owner, address indexed spender, uint256 value);


  function StandardToken() { // Constructor
    organizer = msg.sender;
	balances[organizer] = totalSupply_;
  }
  /**
  * @dev total number of tokens in existence
  */
  function totalSupply() public view returns (uint256) {
    return totalSupply_;
  }
  
  /**
  * @dev Gets the balance of the specified address.
  * @param _owner The address to query the the balance of.
  * @return An uint256 representing the amount owned by the passed address.
  */
  function balanceOf(address _owner) public view returns (uint256 balance) {
    return balances[_owner];
  }
  

  
  /**
  * @dev transfer token for a specified address
  * @param _to The address to transfer to.
  * @param _value The amount to be transferred.
  */
  function transfer(address _to, uint256 _value) public returns (bool) {
    if (balances[msg.sender] >= _value && _value > 0) {
      balances[msg.sender] = balances[msg.sender] - _value;
      balances[_to] = balances[_to] + _value;
      Transfer(msg.sender, _to, _value);
      return true;
    }else{
      return false;
    }
  }


  /**
   * @dev Transfer tokens from one address to another
   * @param _from address The address which you want to send tokens from
   * @param _to address The address which you want to transfer to
   * @param _value uint256 the amount of tokens to be transferred
   */
  function transferFrom(address _from, address _to, uint256 _value) public returns (bool) {
    if (balances[_from] >= _value && allowed[_from][msg.sender] >= _value && _value > 0) {
      balances[_from] = balances[_from] - _value;
      balances[_to] = balances[_to] + _value;
      allowed[_from][msg.sender] = allowed[_from][msg.sender] - _value;
      Transfer(_from, _to, _value);
      return true;
    }else{
      return false;
    }
  }

  /**
   * @dev Approve the passed address to spend the specified amount of tokens on behalf of msg.sender.
   * Beware that changing an allowance with this method brings the risk that someone may use both the old
   * and the new allowance by unfortunate transaction ordering. One possible solution to mitigate this
   * race condition is to first reduce the spender's allowance to 0 and set the desired value afterwards:
   * https://github.com/ethereum/EIPs/issues/20#issuecomment-263524729
   * @param _spender The address which will spend the funds.
   * @param _value The amount of tokens to be spent.
   */
  function approve(address _spender, uint256 _value) public returns (bool) {
    allowed[msg.sender][_spender] = _value;
    Approval(msg.sender, _spender, _value);
    return true;
  }
  
    /**
   * @dev Function to check the amount of tokens that an owner allowed to a spender.
   * @param _owner address The address which owns the funds.
   * @param _spender address The address which will spend the funds.
   * @return A uint256 specifying the amount of tokens still available for the spender.
   */
  function allowance(address _owner, address _spender) public view returns (uint256) {
    return allowed[_owner][_spender];
  }

  function () payable {
  }

 }
