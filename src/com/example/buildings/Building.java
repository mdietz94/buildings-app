package com.example.buildings;

import java.io.Serializable;



public class Building implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1968033627399616496L;
	Integer _id;
	String _name;
	String _country;
	String _state;
	String _city;
	String _region;
	String _address;
	Float _latitude;
	Float _longitude;
	String _date;
	String _description;
	String _architect;
	String[] _keywords;
	Integer[] _images;
	
	// Getters and Setters
	public Float get_latitude() { return _latitude; }
	public Float get_longitude() { return _longitude; }
	public void set_location(float _latitude, float _longitude){
		this._latitude = _latitude;
		this._longitude = _longitude;
	}
	public Integer get_id(){
		return _id;
	}
	public void set_id(Integer _id){
		this._id = _id;
	}
	public String get_name() {
		return _name;
	}
	public void set_name(String _name) {
		if (_name != null)
		this._name = _name;
	}
	public String get_state() {
		return _state;
	}
	public void set_state(String _state) {
		if (_state != null)
		this._state = _state;
	}
	public String get_city() {
		return _city;
	}
	public void set_city(String _city) {
		if (_city != null)
		this._city = _city;
	}
	public String get_country() {
		return _country;
	}
	public void set_country(String _county) {
		if (_county != null)
		this._country = _county;
	}
	public String get_region(){
		return _region;
	}
	public void set_region(String _region){
		if (_region != null)
		this._region = _region;
	}
	public String get_address() {
		return _address;
	}
	public void set_address(String _address) {
		if (_address != null)
		this._address = _address;
	}
	public String get_date() {
		return _date;
	}
	public void set_date(String _date) {
		if (_date != null)
		this._date = _date;
	}
	public String get_description() {
		return _description;
	}
	public void set_description(String _description) {
		if (_description != null)
		this._description = _description;
	}
	public String get_architect() {
		return _architect;
	}
	public void set_architect(String _architect) {
		if (_architect != null)
		this._architect = _architect;
	}
	public String[] get_keywords() {
		return _keywords;
	}
	public void set_keywords(String[] _keywords) {
		if (_keywords != null)
		this._keywords = _keywords;
	}
	public Integer[] get_images() {
		return _images;
	}
	public void set_images(Integer[] _images) {
		this._images = _images;
	}

	public Building()
	{
		_name = ""; _country = ""; _state = ""; _city = ""; _region = "";
		_address = ""; _latitude = 0f; _longitude = 0f; _date = ""; _description = "";
		_architect = ""; _keywords = new String[] {""}; _images = null;
	}

	@Override
    public String toString() {
        String str = _name + (_architect.isEmpty() ? "" :  " by " + _architect) + (_date.isEmpty() ? "" : ", " + _date) +  
        		"\nAddress: " + (_address.isEmpty() ? "" : _address + ", ") + (_region.isEmpty() ? "" : _region + ", ") +
        		(_city.isEmpty() ? "" : _city + ", ") + (_state.isEmpty() ? "" : _state + ", ") + _country +
        		(_latitude > 0 ? "\nCoordinates: " + "(" + _latitude + "," + _longitude + ")" : "") + 
        		(_description.isEmpty() ? "" : "\nDescription: " + _description);
        boolean first = true; // images obviously need to be placed separately.
        for (int i = 0; i < _keywords.length; i++)
        {
        	if (!_keywords[i].isEmpty())
        	{
        		if (first)
        		{
        			str += "\nKeywords: " + _keywords[i];
        			first = false;
        		}
        		else
        		{
        			str += ", " + _keywords[i] ;
        		}
        	}
        }
        str += "\n";
        return str;
    }
	
	

}
