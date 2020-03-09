import React, {useState, useCallback, useEffect} from 'react';
import PropTypes from 'prop-types';
import clsx from 'clsx';
import {v1 as uuid} from 'uuid';
import {useDropzone} from 'react-dropzone';
import PerfectScrollbar from 'react-perfect-scrollbar';
import {makeStyles} from '@material-ui/styles';
import {
  Button,
  IconButton,
  Link,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Typography,
  colors
} from '@material-ui/core';
import FileCopyIcon from '@material-ui/icons/FileCopy';
import ClearIcon from '@material-ui/icons/Clear';
import bytesToSize from 'src/utils/bytesToSize';

const useStyles = makeStyles((theme) => ({
  root: {},
  dropZone: {
    border: `1px dashed ${theme.palette.divider}`,
    padding: theme.spacing(6),
    outline: 'none',
    display: 'flex',
    justifyContent: 'center',
    flexWrap: 'wrap',
    alignItems: 'center',
    '&:hover': {
      backgroundColor: colors.grey[50],
      opacity: 0.5,
      cursor: 'pointer'
    }
  },
  dragActive: {
    backgroundColor: colors.grey[50],
    opacity: 0.5
  },
  image: {
    width: 130
  },
  info: {
    marginTop: theme.spacing(1)
  },
  list: {
    maxHeight: 320
  },
  actions: {
    marginTop: theme.spacing(2),
    display: 'flex',
    justifyContent: 'flex-end',
    '& > * + *': {
      marginLeft: theme.spacing(2)
    }
  }
}));

function FilesDropzone({className, files, onFilesChanged, ...rest}) {
  const classes = useStyles();

  const handleDrop = useCallback((acceptedFiles) => {
    onFilesChanged((prevFiles) => [...prevFiles].concat(acceptedFiles));
  }, []);

  const handleRemove = (file) => {
    onFilesChanged(files.filter(f => f !== file));
  };

  const handleRemoveAll = () => {
    onFilesChanged([]);
  };

  const {getRootProps, getInputProps, isDragActive} = useDropzone({
    onDrop: handleDrop
  });

  return (
    <div
      {...rest}
      className={clsx(classes.root, className)}>
      <div
        className={clsx({
          [classes.dropZone]: true,
          [classes.dragActive]: isDragActive
        })}
        {...getRootProps()}      >
        <input {...getInputProps()} />
        <div>
          <img
            alt="Select file"
            className={classes.image}
            src="/images/undraw_add_file2_gvbb.svg"/>
        </div>
        <div>
          <Typography
            gutterBottom
            variant="h3">
            Select files
          </Typography>
          <Typography
            className={classes.info}
            color="textSecondary"
            variant="body1">
            Drop files here or click
            {' '}
            <Link underline="always">browse</Link>
            {' '}
            thorough your machine
          </Typography>
        </div>
      </div>
      {files.length > 0 && (
        <>
          <PerfectScrollbar options={{suppressScrollX: true}}>
            <List className={classes.list}>
              {files.map((file, i) => (
                <ListItem
                  divider={i < files.length - 1}
                  key={uuid()}>
                  <ListItemIcon>
                    <FileCopyIcon/>
                  </ListItemIcon>
                  <ListItemText
                    primary={file.name}
                    primaryTypographyProps={{variant: 'h5'}}
                    secondary={bytesToSize(file.size)}/>
                  <IconButton edge="end" onClick={() => handleRemove(file)}>
                    <ClearIcon/>
                  </IconButton>
                </ListItem>
              ))}
            </List>
          </PerfectScrollbar>
          <div className={classes.actions}>
            <Button
              onClick={handleRemoveAll}
              size="small">
              Remove all
            </Button>
          </div>
        </>
      )}
    </div>
  );
}

FilesDropzone.propTypes = {
  className: PropTypes.string
};

export default FilesDropzone;
