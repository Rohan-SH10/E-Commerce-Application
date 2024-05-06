import axios from 'axios';
import React, { useState } from 'react';

const AddImage = () => {
    const [productId, setProductId] = useState(0);
    const [imageData, setImageData] = useState({ image: new Uint8Array(), type: '' });
    const [imageType, setImageType] = useState('');
  
    const handleImageChange = (e) => {
      const file = e.target.files[0];
      const reader = new FileReader();
      reader.onload = (e) => {
        const imageData = new Uint8Array(e.target.result);
        const imageType = file.type;
        setImageData({ image: imageData, type: imageType });
      };
      reader.readAsArrayBuffer(file);
    };
  
    const handleImageTypeChange = (e) => {
      setImageType(e.target.value);
    };
  
    const handleImageSubmit = async (e) => {
      e.preventDefault();
      if (!imageData.image || imageData.image.length === 0) {
        console.error('No image selected');
        return; // or display an error message to the user
      }
  
      try {
        const blob = new Blob([imageData.image], { type: imageData.type });
        const formData = new FormData();
        formData.append('image', blob);
  
        const response = await axios.post(
          `http://localhost:8080/api/re-v1/product/${productId}/add-image?imageType=${imageType}`,
          formData,
          {
            headers: {
              'Content-Type': 'multipart/form-data'
            },
            withCredentials: true
          }
        );
        console.log(response.data);
      } catch (error) {
        console.error('Error uploading image:', error);
        // Consider displaying an error message to the user or retrying the request
      }
    };
  
    return (
      <form onSubmit={handleImageSubmit} className="grid grid-cols-2 gap-4">
        <div>
          <label htmlFor='image' >Product Id:</label>
          <input
            type="tel"
            placeholder='Product Id'
            value={productId}
            onChange={(e) => setProductId(e.target.value)}
            className="border p-2 w-full"
            required
          />
        </div>
        <div>
          <label htmlFor='image' >Image Type:</label>
          <select
            value={imageType}
            placeholder='Image Type'
            onChange={handleImageTypeChange}
            className="border p-2 w-full bg-white"
            required
          >
            <option value="">-- Select Image Type --</option>
            <option value="COVER">Cover</option>
            <option value="OTHER">Other</option>
          </select>
        </div>
        <div>
          <label htmlFor='image' >Image Upload:</label>
          <input
            name='image'
            id='image'
            type="file" accept=".jpg,.jpeg,.png" multiple
            onChange={handleImageChange}
            className="border p-2 w-full"
            required
          />
        </div>
        <button type="submit"
          className="bg-blue-500 text-white px-4 py-2 rounded mr-2">
          Add Image
        </button>
      </form>
    );
  };
  
  export default AddImage;