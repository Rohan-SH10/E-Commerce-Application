import React from 'react'

const Label = ({htmlFor,label}) => {
  return (
    <label 
        className='p-1 absolute left-0 -top-3.5 text-gray-600 text-sm peer-placeholder-shown:text-base peer-placeholder-shown:text-gray-440
        peer-placeholder-shown:top-2 transition-all peer-focus:-top-3.5 peer-focus:text-gray-600 peer-focus:text-sm'
        htmlFor={htmlFor}
        >
      {label}
    </label>
  )
}

export default Label
