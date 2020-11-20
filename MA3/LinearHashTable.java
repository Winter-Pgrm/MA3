/*
 *  Microassignment: Probing Hash Table addElement and removeElement
 *
 *  LinearHashTable: Yet another Hash Table Implementation
 * 
 *  Contributors:
 *    Bolong Zeng <bzeng@wsu.edu>, 2018
 *    Aaron S. Crandall <acrandal@wsu.edu>, 2019
 * 
 *  Copyright:
 *   For academic use only under the Creative Commons
 *   Attribution-NonCommercial-NoDerivatives 4.0 International License
 *   http://creativecommons.org/licenses/by-nc-nd/4.0
 */


class LinearHashTable<K, V> extends HashTableBase<K, V>
{
	// Linear and Quadratic probing should rehash at a load factor of 0.5 or higher
    private static final double REHASH_LOAD_FACTOR = 0.5;

    // Constructors
    public LinearHashTable()
    {
        super();
    }

    public LinearHashTable(HasherBase<K> hasher)
    {
        super(hasher);
    }

    public LinearHashTable(HasherBase<K> hasher, int number_of_elements)
    {
        super(hasher, number_of_elements);
    }

    // Copy constructor
    public LinearHashTable(LinearHashTable<K, V> other)
    {
        super(other);
	}
    
   
    // ***** MA Section Start ************************************************ //
	/*
	*  Microassignment3: Probing Hash Table addElement and removeElement
	*
	*  LinearHashTable: Yet another Hash Table Implementation
	* 
	*  Contributors:
	*    Alexander Phillips 
	* 
	*/
    // Concrete implementation for parent's addElement method
    public void addElement(K key, V value)
    {
		// Check for size restrictions
		resizeCheck();
 
		// Calculate hash based on key
		int hash = super.getHash(key);

		// MA TODO: find empty slot to insert (update HashItem as necessary)
		
		//Make a Hashitem to get access to the goods
		HashItem<K, V> ct = _items.elementAt(hash);
				
		//If the Bucket is empty you are good to place it here!
		//Just set the key, value, deleted flag and update the elements
		if(ct.isEmpty() == true)
		{
		
			ct.setKey(key);
			ct.setValue(value);
			ct.setIsEmpty(false);
			_number_of_elements++;
		}
		//All other options fall here!
		else
		{
			//Flag to get out of the do while loop
			Boolean placed = false;
			//i is the addtion to the offset 
			int i = 0, offset = hash;
			do
			{
				//Linear probing
				offset = (hash +i) % _items.size();
				//Check the newest 
				ct = _items.elementAt(offset);
				//if the bucket is empty or is the same key to update
				if(ct.getKey() == null || ct.getKey().equals(key))
				{
				//set the all the data for the bucket
					ct.setKey(key);
					ct.setValue(value);
					ct.setIsEmpty(false);
					//flag is triggered to exit the loop
					placed = true;
					_number_of_elements++;
				}
				i++;
			}
			while(placed == false);
		}
        // Remember how many things we are presently storing (size N)
    	//  Hint: do we always increase the size whenever this function is called?
        // _number_of_elements++;

    }

    // Removes supplied key from hash table
    public void removeElement(K key)
    {
        // Calculate hash from key
        int hash = super.getHash(key);

        // MA TODO: find slot to remove. Remember to check for infinite loop!
        //  ALSO: Use lazy deletion - see structure of HashItem
		
    	//Make a Hashitem to get access to the goods
        HashItem<K, V> removeHT = _items.elementAt(hash);
 
		//Set the offset, and the flag for the do while
		int i = 0;
		Boolean repeat = true;
		int offset = hash;
		
		do
		{
			// Update the hashitem as needed to go thru 
			removeHT = _items.elementAt(offset);
			//if the hashitem has a key 
			if(removeHT.getKey() != null)
			{
				//if the key in the bucket IS the same as key
				if(removeHT.getKey().equals(key))
				{
					//Than we soft delete it. 
					removeHT.setIsEmpty(true);
					_number_of_elements--;
					//set the flag to get out of the loop
					repeat = false;
              
				}
              
			}
			//update i offset 
			i++;
			//Linear probing
			offset = (hash +i) % _items.size();
		}
		////if the flag is false or we went thru the whole hashtable once LEAVE!
		while((repeat = true) && (offset != hash));
        // Make sure decrease hashtable size
    	//  Hint: do we always reduce the size whenever this function is called?
        // _number_of_elements--;
        
    }
    
    // ***** MA Section End ************************************************ //
    

    // Public API to get current number of elements in Hash Table
	public int size() {
		return this._number_of_elements;
	}

    // Public API to test whether the Hash Table is empty (N == 0)
	public boolean isEmpty() {
		return this._number_of_elements == 0;
	}
    
    // Returns true if the key is contained in the hash table
    public boolean containsElement(K key)
    {
        int hash = super.getHash(key);
        HashItem<K, V> slot = _items.elementAt(hash);
        
        // Left incomplete to avoid hints in the MA :)
        return false;
    }
    
    // Returns the item pointed to by key
    public V getElement(K key)
    {
        int hash = super.getHash(key);
        HashItem<K, V> slot = _items.elementAt(hash);
        
        // Left incomplete to avoid hints in the MA :)
        return null;
    }

    // Determines whether or not we need to resize
    //  to turn off resize, just always return false
    protected boolean needsResize()
    {
        // Linear probing seems to get worse after a load factor of about 50%
        if (_number_of_elements > (REHASH_LOAD_FACTOR * _primes[_local_prime_index]))
        {
            return true;
        }
        return false;
    }
    
    // Called to do a resize as needed
    protected void resizeCheck()
    {
        // Right now, resize when load factor > 0.5; it might be worth it to experiment with 
        //  this value for different kinds of hashtables
        if (needsResize())
        {
            _local_prime_index++;

            HasherBase<K> hasher = _hasher;
            LinearHashTable<K, V> new_hash = new LinearHashTable<K, V>(hasher, _primes[_local_prime_index]);

            for (HashItem<K, V> item: _items)
            {
                if (item.isEmpty() == false)
                {
                    // Add element to new hash table
                    new_hash.addElement(item.getKey(), item.getValue());
                }
            }

            // Steal temp hash object's internal vector for ourselves
            _items = new_hash._items;
        }
    }

    // Debugging tool to print out the entire contents of the hash table
	public void printOut() {
		System.out.println(" Dumping hash with " + _number_of_elements + " items in " + _items.size() + " buckets");
		System.out.println("[X] Key	| Value	| Deleted");
		for( int i = 0; i < _items.size(); i++ ) {
			HashItem<K, V> curr_slot = _items.get(i);
			System.out.print("[" + i + "] ");
			System.out.println(curr_slot.getKey() + " | " + curr_slot.getValue() + " | " + curr_slot.isEmpty());
		}
	}
}