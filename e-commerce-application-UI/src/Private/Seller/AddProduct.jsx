import axios from 'axios';
import React, { useState } from 'react';

const AddProduct = () => {
  // State variables for product details
  const [productName, setProductName] = useState('');
  const [productDescription, setProductDescription] = useState('');
  const [productPrice, setProductPrice] = useState('');
  const [productQuantity, setProductQuantity] = useState('');
  const [category, setCategory] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    console.log({
      productName,
      productDescription,
      productPrice,
      productQuantity,
      category,
    });
    try {
      let { data: { data } } = await axios.post(`http://localhost:8080/api/re-v1/products/product`, {
        productName: productName,
        productDescription: productDescription,
        productPrice: productPrice,
        productQuantity: productQuantity,
        category: category,
      }, {
        headers: {
          "Content-Type": "application/json"
        },
        withCredentials: true
      })
      console.log("after api call", data)
      // Perform validation and submit data to backend or perform other actions
      // Reset form fields
      setProductName('');
      setProductDescription('');
      setProductPrice('');
      setProductQuantity('');
      setCategory('');
    } catch (error) {
      console.log(error)
    }
  };

  return (
    <div>
      <form onSubmit={handleSubmit} className="grid grid-cols-2 gap-4">
        {/* Product form fields */}
        <div>
          <label htmlFor='productName' >Product Name:</label>
          <input
            type="text"
            placeholder='Product Name'
            value={productName}
            onChange={(e) => setProductName(e.target.value)}
            className="border p-2 w-full"
            required
          />
        </div>
        <div>
          <label htmlFor='productDescription' >Product Description:</label>
          <textarea
            value={productDescription}
            placeholder='Product Description'
            onChange={(e) => setProductDescription(e.target.value)}
            className="border p-2 w-full col-span-2"
            required
          />
        </div>
        <div>
          <label htmlFor='productPrice' >Product Price:</label>
          <input
            type="number"
            placeholder='Product Price'
            value={productPrice}
            onChange={(e) => setProductPrice(e.target.value)}
            className="border p-2 w-full"
            required
          />
        </div>
        <div>
          <label htmlFor='productQuantity' >Product Quantity:</label>
          <input
            type="number"
            placeholder='Product Quantity'
            value={productQuantity}
            onChange={(e) => setProductQuantity(e.target.value)}
            className="border p-2 w-full"
            required
          />
        </div>
        <div>
          <label htmlFor='category' >Product Category:</label>
          <select
            value={category}
            placeholder='Product Category'
            onChange={(e) => setCategory(e.target.value)}
            className="border p-2 w-full bg-white"
            required
          >
            <option value="">-- Select Category --</option>
            <option value="MOBILE">Mobiles</option>
            <option value="PC">PC's</option>
            <option value="AUDIO">Audio</option>
            <option value="GAMING">Gaming</option>
            <option value="CAMERA">Camera</option>
            <option value="ACCESSORIES">Accessories</option>
            <option value="PERSONALCARE">Personal Care</option>
          </select>
        </div>
        <button type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded mr-2">
          Add Product
        </button>
      </form>
    </div>
  );
};

export default AddProduct;